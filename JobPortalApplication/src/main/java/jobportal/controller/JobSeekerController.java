package jobportal.controller;

import jobportal.model.Application;
import jobportal.service.ApplicationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seeker")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Job Seeker", description = "Job seeker specific endpoints for job applications")
public class JobSeekerController {

    private final ApplicationService applicationService;

    public JobSeekerController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @PostMapping("/apply/{jobId}")
    @Operation(summary = "Apply for a job", description = "Submit application for a specific job")
    public ResponseEntity<Application> applyForJob(@PathVariable Long jobId, Authentication authentication) {
        Application application = applicationService.applyForJob(jobId, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(application);
    }

    @GetMapping("/applications")
    @Operation(summary = "Get my applications", description = "Retrieve all job applications submitted by the logged-in job seeker")
    public ResponseEntity<Page<Application>> getMyApplications(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            Authentication authentication) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Application> applications = applicationService.getJobSeekerApplications(authentication.getName(), pageable);
        return ResponseEntity.status(HttpStatus.OK).body(applications);
    }
}