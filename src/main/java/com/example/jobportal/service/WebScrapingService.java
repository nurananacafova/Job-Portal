package com.example.jobportal.service;

import com.example.jobportal.dto.JobDto;
import com.example.jobportal.enums.Industry;
import com.example.jobportal.model.Job;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebScrapingService {
    private static final String URL = "https://djinni.co/jobs/";

//    public List<JobDto> scrapeJobs() throws IOException {
//        List<JobDto> jobs = new ArrayList<>();
//        Document doc = Jsoup.connect(URL).get();
//        Elements elements = doc.select("ul.list-jobs > li");
//
//        for (Element jobElement : elements) {
//            JobDto job = new JobDto();
//
//            Element title = jobElement.selectFirst("h2 > a");
//            if (title != null) {
//                job.setJobTitle(title.text());
//            }
//            Element company = jobElement.selectFirst("a[data-analytics='company_page']");
//            if (company != null) {
//                job.setCompanyName(company.text());
//            }
//
//            // Full description (which includes location, format, experience)
//            Element jobDescription = jobElement.selectFirst("div.job-details");
//            if (jobDescription != null) {
////                String description = jobDescription.text();
//                job.setJobDescription(jobDescription.text());
//                // You can later split this or parse more accurately if needed
//            }
//
//            // Work format & location
//            Element jobMeta = jobElement.selectFirst("div:contains(Full Remote), div:contains(Office Work)");
//            if (jobMeta != null) {
//                String[] parts = jobMeta.text().split("·");
//                if (parts.length >= 1) job.setJobType(parts[0].trim());
//                if (parts.length >= 2) job.setLocation(parts[1].trim());

    /// /                if (parts.length >= 3) job.setExperienceLevel(parts[2].trim());
//            }
//
//            // Experience
//            Element exp = jobElement.selectFirst("div.text-nowrap)");
//            if (exp != null) {
//                job.setExperienceLevel(exp.text());
//            }
//
//            // Salary
//            Element salaryEl = jobElement.selectFirst("strong.text-success");
//            if (salaryEl != null) {
//                job.setSalaryRange(salaryEl.text());
//            }
//
//            jobs.add(job);
//        }
//        return jobs;
//
//    }
    public List<JobDto> scrapeJobs() {
        List<JobDto> jobs = new ArrayList<>();

        try {
            Document doc = Jsoup.connect(URL).timeout(20_000).get();
            Elements jobElements = doc.select("ul.list-jobs > li"); // Each job card
            log.info("Job elements: {}", jobElements.toString());
            for (Element jobElement : jobElements) {
                JobDto job = parseJob(jobElement);
                jobs.add(job);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return jobs;
    }

    private JobDto parseJob(Element jobElement) {
        JobDto job = new JobDto();

        Element titleEl = jobElement.selectFirst("h2 > a.job-item__title-link");
        if (titleEl != null) {
            job.setJobTitle(titleEl.text());
            job.setSource("https://djinni.co" + titleEl.attr("href"));
        }

        Element companyEl = jobElement.selectFirst("a[data-analytics='company_page']");
        if (companyEl != null) {
            job.setCompanyName(companyEl.text());
        }

        Elements infoSpans = jobElement.select("span.text-nowrap");
        for (Element span : infoSpans) {
            String text = span.text();
            if (text.contains("Remote") || text.contains("Office")) {
                job.setJobType(text);
            } else if (text.matches(".*\\d+.*year.*")) {
                job.setExperienceLevel(text);
            } else if (text.equalsIgnoreCase("Upper-Intermediate") || text.equalsIgnoreCase("Intermediate")) {
                job.setEducationLevel(text); // using it as a proxy
            }
        }

        Element locationEl = jobElement.selectFirst("span.location-text");
        if (locationEl != null) {
            job.setLocation(locationEl.text());
        }

        Element salaryEl = jobElement.selectFirst("span.text-success");
        if (salaryEl != null) {
            job.setSalaryRange(salaryEl.text());
        }

        Element descEl = jobElement.selectFirst("div[id^=job-description] > span");
        if (descEl != null) {
            String fullText = descEl.text();
            String[] parts = fullText.split("Requirements:");
            job.setJobDescription(parts[0].trim());
            if (parts.length > 1) {
                job.setRequirements(parts[1].trim());
            }
        }

        Element dateEl = jobElement.selectFirst("span[data-original-title]");
        if (dateEl != null) {
            String dateText = dateEl.attr("data-original-title");
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            try {
                job.setPostedDate(LocalDate.parse(dateText, formatter));
                job.setApplicationDeadline(job.getPostedDate().plusWeeks(2));
            } catch (DateTimeParseException e) {
                // ignore
            }
        }

        job.setIndustry(Industry.TECH); // hardcoded for now

        Elements tagElements = jobElement.select("div.job-list-item__tags span");
        List<String> tags = tagElements.stream()
                .map(Element::text)
                .collect(Collectors.toList());
        job.setTags(tags);

        job.setBenefits(List.of("Recruitment interview", "Technical interview", "Code review")); // example
        job.setHowToApply("Apply via Djinni"); // or set as needed

        Element logoEl = jobElement.selectFirst("img[src*='logo']");
        if (logoEl != null) {
            job.setCompanyLogo(logoEl.attr("src"));
        }

        return job;
    }

//    private final List<Job> jobs = new ArrayList<>();
//    private static Document document = null;
//
//    public static List<Job> getJobs() {
//        try {
//            document = Jsoup.connect("https://djinni.co/jobs/").get();
////            var tbody = document.getElementById("thetable").getElementsByTag("tbody");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Elements elements =document.select("div.weapper")
//    }
}
