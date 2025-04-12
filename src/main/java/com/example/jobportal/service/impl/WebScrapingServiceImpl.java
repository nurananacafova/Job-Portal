package com.example.jobportal.service.impl;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.mapper.ModelMapper;
import com.example.jobportal.model.Job;
import com.example.jobportal.repository.JobRepository;
import com.example.jobportal.service.WebScrapingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebScrapingServiceImpl implements WebScrapingService {
    private static final String URL = "https://djinni.co/jobs/";
    private static final String BASE_URL = "https://djinni.co";
    private final ModelMapper modelMapper;
    private final JobRepository jobRepository;

    @Override
    public void scrapeAndSaveJobs() throws IOException {
        log.info("Request accepted for scraping jobs at: {}", LocalDateTime.now());
        List<JobDto> jobs = new ArrayList<>();
        Document doc = Jsoup.connect(URL).timeout(20000).get();
        Elements jobElements = doc.select("ul.list-jobs > li");

        for (Element jobElement : jobElements) {
            JobDto jobDto = new JobDto();

            Element titleEl = jobElement.selectFirst("a.job-item__title-link");
            String jobDetailUrl = titleEl != null ? BASE_URL + titleEl.attr("href") : null;
            jobDto.setSource(jobDetailUrl);
            jobDto.setJobTitle(titleEl != null ? titleEl.text().trim() : null);

            Element companyEl = jobElement.selectFirst("a[data-analytics='company_page']");
            jobDto.setCompanyName(companyEl != null ? companyEl.text().trim() : null);

            Element locationEl = jobElement.selectFirst("span.location-text");
            jobDto.setLocation(locationEl != null ? locationEl.text().trim() : null);

            Element jobMeta = jobElement.selectFirst("div:contains(Full Remote), div:contains(Office Work)");
            if (jobMeta != null) {
                String[] parts = jobMeta.text().split("·");
                if (parts.length >= 1) jobDto.setJobType(parts[0].trim());
            }

            Element experienceElement = jobElement.select("div.fw-medium span.text-nowrap:contains(Experience)").first();
            jobDto.setExperienceLevel(experienceElement != null ? experienceElement.text().trim() : null);

            Element salaryEl = jobElement.selectFirst("span.text-success");
            jobDto.setSalaryRange(salaryEl != null ? salaryEl.text().trim() : "not mentioned");

//            Element logoEl = jobElement.selectFirst("img[src*='logo']");
//            job.setCompanyLogo(logoEl != null ? BASE_URL + logoEl.attr("src") : null);
            Element logoEl = jobElement.selectFirst("img.userpic-image");
            String logoUrl = logoEl != null ? logoEl.attr("src") : null;
            jobDto.setCompanyLogo(logoUrl != null ? logoUrl : null);

            if (jobDetailUrl != null) {
                Document detailDoc = Jsoup.connect(jobDetailUrl).timeout(20000).get();

                Element postedDateEl = detailDoc.selectFirst("span[class='font-weight-500']");

                LocalDate date = parseToDate(postedDateEl != null ? postedDateEl.text().trim() : null);
                jobDto.setPostedDate(postedDateEl != null ? parseToDate(postedDateEl.text().trim()) : null);

                Element descEl = detailDoc.selectFirst("div.job-post__description");
                jobDto.setJobDescription(descEl != null ? descEl.text().trim() : null);

                Element reqEl = detailDoc.selectFirst(".job-additional-info");
                jobDto.setRequirements(reqEl != null ? reqEl.text().trim() : null);

                jobDto.setEducationLevel("Not specified");


                Element benefitsEl = detailDoc.selectFirst("div:matchesOwn(Benefits)");
                jobDto.setBenefits(benefitsEl != null ? benefitsEl.text().trim() : "Not listed");


                jobDto.setApplicationDeadline(jobDto.getPostedDate() != null ? jobDto.getPostedDate().plusWeeks(2) : null);
                Element applyButton = detailDoc.selectFirst("a.btn-primary");
                jobDto.setHowToApply(applyButton != null ? applyButton.absUrl("href") : null);

                Elements tagElements = detailDoc.select("#job_extra_info .row .col-auto + .col");
                List<String> allTags = new ArrayList<>();
                for (Element tagElement : tagElements) {
                    List<String> tags = Arrays.stream(tagElement.text().split(","))
                            .map(String::trim)
                            .map(String::toLowerCase)
                            .collect(Collectors.toList());
                    allTags.addAll(tags);
                }
                jobDto.setTags(allTags);

                jobDto.setIndustry(detectIndustry(jobDto.getTags(), jobDto.getJobDescription(), jobDto.getJobTitle()));

            }


            boolean isRemote = jobDto.getJobType() != null &&
                    jobDto.getJobType().toLowerCase().contains("full remote");
            boolean isWorldwide = jobDto.getLocation() != null &&
                    jobDto.getLocation().toLowerCase().contains("worldwide");
            boolean offersRelocation = jobDto.getJobType() != null &&
                    jobDto.getJobType().toLowerCase().contains("office work");
            boolean mentionsAzerbaijan = jobDto.getLocation() != null &&
                    jobDto.getLocation().toLowerCase().contains("azerbaijan");

            if ((isRemote && isWorldwide) || (isRemote && mentionsAzerbaijan) || offersRelocation) {
                jobs.add(jobDto);
                log.info("Job Added: {}", jobDto.getJobTitle());
            } else {
                log.info("Skipped: {}", jobDto.getJobTitle());
            }
        }
        List<Job> jobList = modelMapper.jobDtoListToJobList(jobs);
        jobRepository.saveAll(jobList);
        log.info("{} jobs saved to the database", jobList.size());
        log.info("Request ended for scraping jobs at: {}", LocalDateTime.now());
    }

    public static String detectIndustry(List<String> tags, String description, String title) {
        String combined = (String.join(" ", tags) + " " + description + " " + title).toLowerCase();
        String industry = null;
        if (containsAny(combined, "fintech", "finance", "bank", "financial", "e-commerce", "retail", "shop", "store")) {
            industry = "Finance";
        } else if (containsAny(combined, "health", "medtech", "pharma", "hospital")) {
            industry = "Healthcare";
        } else if (containsAny(combined, "security", "cybersecurity", "infosec", "game", "gamedev", "unity", "unreal", "ai", "ml", "machine learning", "artificial intelligence", "crypto", "blockchain", "web3", "nft")) {
            industry = "Tech";
        } else if (containsAny(combined, "education", "edtech", "e-learning")) {
            industry = "Education";
        } else if (containsAny(combined, "travel", "tourism", "hospitality")) {
            industry = "Travel";
        } else {
            industry = "Tech";
        }
        return industry;
    }

    public static LocalDate parseToDate(String input) {
        if (input == null) {
            return null;
        }
        String[] parts = input.trim().split(" ");
        if (parts.length != 3) {
            throw new IllegalArgumentException("Invalid date format");
        }

        String month = parts[2];
        int day = Integer.parseInt(parts[1]);
        int year = LocalDate.now().getYear();
        int monthNumber = Month.valueOf(month.toUpperCase(Locale.ENGLISH)).getValue();

        return LocalDate.of(year, monthNumber, day);
    }

    private static boolean containsAny(String text, String... keywords) {
        for (String keyword : keywords) {
            if (text.contains(keyword)) {
                return true;
            }
        }
        return false;
    }
}

