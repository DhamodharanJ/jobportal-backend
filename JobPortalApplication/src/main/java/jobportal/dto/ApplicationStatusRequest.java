package jobportal.dto;

import jobportal.model.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Application status update request")
public class ApplicationStatusRequest {

    @NotNull(message = "Status is required")
    @Schema(description = "New status for the application", example = "SHORTLISTED", required = true)
    private ApplicationStatus status;
}