package jobportal.scheduler;

import jobportal.model.Job;
import jobportal.model.JobStatus;
import jobportal.repository.JobRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class JobExpiryScheduler {

    private final JobRepository jobRepository;

    public JobExpiryScheduler(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    @CacheEvict(value = {"jobs", "activeJobs"}, allEntries = true)
    public void expireOldJobs() {
        log.info("Job expiry scheduler started");

        LocalDate today = LocalDate.now();
        List<Job> expiredJobs = jobRepository.findExpiredJobs(today);

        if (expiredJobs.isEmpty()) {
            log.info("No jobs to expire today");
            return;
        }

        for (Job job : expiredJobs) {
            job.setStatus(JobStatus.EXPIRED);
            jobRepository.save(job);
            log.info("Job expired - ID: {}, Title: {}", job.getJobId(), job.getTitle());
        }

        log.info("Job expiry scheduler completed. Total jobs expired: {}", expiredJobs.size());
    }
}