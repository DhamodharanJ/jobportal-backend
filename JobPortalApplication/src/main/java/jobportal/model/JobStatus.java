package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a job posting")
public enum JobStatus {
    @Schema(description = "Job is currently active and accepting applications")
    ACTIVE,

    @Schema(description = "Job has been closed by recruiter")
    CLOSED,

    @Schema(description = "Job has expired based on expiry date")
    EXPIRED
}