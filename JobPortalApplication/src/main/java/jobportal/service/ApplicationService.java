package jobportal.service;

import jobportal.dto.ApplicationStatusRequest;
import jobportal.exception.CustomException;
import jobportal.model.*;
import jobportal.repository.ApplicationRepository;
import jobportal.repository.JobRepository;
import jobportal.repository.JobSeekerRepository;
import jobportal.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ApplicationService {

    private final ApplicationRepository applicationRepository;
    private final JobRepository jobRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;

    public ApplicationService(ApplicationRepository applicationRepository, JobRepository jobRepository,
                              JobSeekerRepository jobSeekerRepository, UserRepository userRepository) {
        this.applicationRepository = applicationRepository;
        this.jobRepository = jobRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public Application applyForJob(Long jobId, String email) {
        log.info("Job application request for job ID: {} by email: {}", jobId, email);

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Job seeker profile not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));

        if (job.getStatus() != JobStatus.ACTIVE) {
            log.error("Application failed: Job is not active - ID: {}", jobId);
            throw new CustomException("This job is no longer accepting applications");
        }

        if (applicationRepository.existsByJobAndJobSeeker(job, jobSeeker)) {
            log.error("Application failed: Already applied - Job ID: {}, Email: {}", jobId, email);
            throw new CustomException("You have already applied for this job");
        }

        Application application = new Application();
        application.setJob(job);
        application.setJobSeeker(jobSeeker);
        application.setStatus(ApplicationStatus.APPLIED);

        application = applicationRepository.save(application);
        log.info("Application submitted successfully - ID: {} for job: {} by email: {}",
                application.getApplicationId(), jobId, email);
        return application;
    }

    public Page<Application> getJobSeekerApplications(String email, Pageable pageable) {
        log.info("Fetching applications for email: {}", email);

        // Changed: findByUsername → findByEmail
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findByUser(user)
                .orElseThrow(() -> new CustomException("Job seeker profile not found"));

        return applicationRepository.findByJobSeeker(jobSeeker, pageable);
    }

    public Page<Application> getJobApplications(Long jobId, String email, Pageable pageable) {
        log.info("Fetching applications for job ID: {} by email: {}", jobId, email);

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new CustomException("Job not found"));
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        if (!job.getRecruiter().getUser().getUserId().equals(user.getUserId())) {
            log.error("Unauthorized access attempt to job applications by email: {}", email);
            throw new CustomException("You are not authorized to view these applications");
        }

        return applicationRepository.findByJob(job, pageable);
    }

    @Transactional
    public Application updateApplicationStatus(Long applicationId, ApplicationStatusRequest request, String email) {
        log.info("Application status update request for ID: {} by email: {}", applicationId, email);

        Application application = applicationRepository.findById(applicationId)
                .orElseThrow(() -> new CustomException("Application not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new CustomException("User not found"));

        if (!application.getJob().getRecruiter().getUser().getUserId().equals(user.getUserId())) {
            log.error("Unauthorized status update attempt by email: {}", email);
            throw new CustomException("You are not authorized to update this application");
        }

        application.setStatus(request.getStatus());
        application = applicationRepository.save(application);
        log.info("Application status updated to {} for application ID: {}", request.getStatus(), applicationId);
        return application;
    }
}