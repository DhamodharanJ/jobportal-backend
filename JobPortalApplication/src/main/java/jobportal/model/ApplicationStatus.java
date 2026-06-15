package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of a job application")
public enum ApplicationStatus {
    @Schema(description = "Application has been submitted")
    APPLIED,

    @Schema(description = "Candidate has been shortlisted")
    SHORTLISTED,

    @Schema(description = "Application has been rejected")
    REJECTED,

    @Schema(description = "Candidate has been hired")
    HIRED
}