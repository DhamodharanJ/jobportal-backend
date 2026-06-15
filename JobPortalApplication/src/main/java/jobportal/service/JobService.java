package jobportal.service;

import jobportal.dto.JobRequest;
import jobportal.exception.CustomException;
import jobportal.model.*;
import jobportal.repository.JobRepository;
import jobportal.repository.RecruiterRepository;
import jobportal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@Slf4j
public class JobService {

    private final JobRepository jobRepository;
    private final RecruiterRepository recruiterRepository;
    private final UserRepository userRepository;

    public JobService(JobRepository jobRepository, RecruiterRepository recruiterRepository,
                      UserRepository userRepository) {
        this.jobRepository = jobRepository;
        this.recruiterRepository = recruiterRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    @CacheEvict(value = "activeJobs", allEntries = true)
    public Job createJob(JobRequest request, String email) {
        log.info("Job creation request from email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        Recruiter recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Recruiter profile not found"));

        Job job = new Job();
        job.setRecruiter(recruiter);
        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setSkills(request.getSkills());
        job.setStatus(JobStatus.ACTIVE);
        job.setPostedDate(LocalDate.now());
        job.setExpiryDate(request.getExpiryDate());

        job = jobRepository.save(job);
        log.info("Job created successfully with ID: {} by email: {}", job.getJobId(), email);
        return job;
    }

    @Cacheable(value = "activeJobs")
    public Page<Job> getAllActiveJobs(Pageable pageable) {
        log.info("Fetching active jobs - Page: {}, Size: {}", pageable.getPageNumber(), pageable.getPageSize());
        return jobRepository.findByStatus(JobStatus.ACTIVE, pageable);
    }

    public Page<Job> getRecruiterJobs(String email, Pageable pageable) {
        log.info("Fetching jobs for email: {}", email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        Recruiter recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Recruiter profile not found"));

        return jobRepository.findByRecruiter(recruiter, pageable);
    }

    public Job getJobById(Long jobId) {
        log.info("Fetching job details for ID: {}", jobId);
        return jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found with id: " + jobId));
    }

    @Transactional
    @CacheEvict(value = "activeJobs", allEntries = true)
    public Job updateJob(Long jobId, JobRequest request, String email) {
        log.info("Job update request for ID: {} by email: {}", jobId, email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        Recruiter recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Recruiter profile not found"));

        if (!job.getRecruiter().getRecruiterId().equals(recruiter.getRecruiterId())) {
            log.error("Unauthorized job update attempt by email: {}", email);
            throw new CustomException("You are not authorized to update this job");
        }

        job.setTitle(request.getTitle());
        job.setDescription(request.getDescription());
        job.setLocation(request.getLocation());
        job.setSalary(request.getSalary());
        job.setSkills(request.getSkills());
        job.setExpiryDate(request.getExpiryDate());

        job = jobRepository.save(job);
        log.info("Job updated successfully: {}", jobId);
        return job;
    }

    @Transactional
    @CacheEvict(value = "activeJobs", allEntries = true)
    public void closeJob(Long jobId, String email) {
        log.info("Job close request for ID: {} by email: {}", jobId, email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        Recruiter recruiter = recruiterRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Recruiter profile not found"));

        if (!job.getRecruiter().getRecruiterId().equals(recruiter.getRecruiterId())) {
            log.error("Unauthorized job close attempt by email: {}", email);
            throw new CustomException("You are not authorized to close this job");
        }

        job.setStatus(JobStatus.CLOSED);
        jobRepository.save(job);
        log.info("Job closed successfully: {}", jobId);
    }
}