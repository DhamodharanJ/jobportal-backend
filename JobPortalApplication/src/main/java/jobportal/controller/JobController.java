package jobportal.controller;

import jobportal.model.Job;
import jobportal.service.JobService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Jobs", description = "Job listing endpoints accessible to all authenticated users")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    @Operation(summary = "Get all active jobs", description = "Retrieve paginated list of active jobs")
    public ResponseEntity<Page<Job>> getAllActiveJobs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<Job> jobs = jobService.getAllActiveJobs(pageable);
        return ResponseEntity.status(HttpStatus.OK).body(jobs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get job by ID", description = "Retrieve detailed information about a specific job")
    public ResponseEntity<Job> getJobById(@PathVariable Long id) {
        Job job = jobService.getJobById(id);
        return ResponseEntity.status(HttpStatus.OK).body(job);
    }
}