package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "User roles in the system")
public enum Role {

    @Schema(description = "Recruiter who can post jobs")
    RECRUITER,

    @Schema(description = "Job seeker who can apply for jobs")
    JOB_SEEKER
}