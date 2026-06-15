package jobportal.controller;

import jobportal.dto.ApplicationStatusRequest;
import jobportal.dto.JobRequest;
import jobportal.model.Application;
import jobportal.model.Job;
import jobportal.service.ApplicationService;
import jobportal.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recruiter")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Recruiter", description = "Recruiter specific endpoints for job and application management")
public class RecruiterController {

    private final JobService jobService;
    private final ApplicationService applicationService;

    public RecruiterController(JobService jobService, ApplicationService applicationService) {
        this.jobService = jobService;
        this.applicationService = applicationService;
    }

    @PostMapping("/jobs")
    @Operation(summary = "Post a new job", description = "Create a new job posting")
    public ResponseEntity<Job> createJob(@Valid @RequestBody JobRequest request,
                                         Authentication authentication) {
        Job job = jobService.createJob(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(job);
    }

    @GetMapping("/jobs")
    @Operation(summary = "Get my posted jobs", description = "Retrieve all jobs posted by the logged-in recruiter")
    public ResponseEntity<Page<Job>> getMyJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getRecruiterJobs(authentication.getName(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @PutMapping("/jobs/{id}")
    @Operation(summary = "Update job", description = "Update an existing job posting")
    public ResponseEntity<Job> updateJob(@PathVariable Long id,
                                         @Valid @RequestBody JobRequest request,
                                         Authentication authentication) {
        Job job = jobService.updateJob(id, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }

    @PutMapping("/jobs/{id}/close")
    @Operation(summary = "Close job", description = "Close a job posting")
    public ResponseEntity<Void> closeJob(@PathVariable Long id, Authentication authentication) {
        jobService.closeJob(id, authentication.getName());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping("/applications/{jobId}")
    @Operation(summary = "Get job applications", description = "Retrieve all applications for a specific job")
    public ResponseEntity<Page<Application>> getJobApplications(
            @PathVariable Long jobId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applications = applicationService.getJobApplications(jobId, authentication.getName(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }

    @PutMapping("/applications/{id}/status")
    @Operation(summary = "Update application status", description = "Update the status of a job application")
    public ResponseEntity<Application> updateApplicationStatus(
            @PathVariable Long id,
            @Valid @RequestBody ApplicationStatusRequest request,
            Authentication authentication) {

        Application application = applicationService.updateApplicationStatus(id, request, authentication.getName());
        return ResponseEntity.status(HttpStatus.OK).body(application);
    }
}