package jobportal.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDate;

@Data
@Schema(description = "Job creation/update request payload")
public class JobRequest {

    @NotBlank(message = "Title is required")
    @Schema(description = "Job title", example = "Senior Java Developer", required = true)
    private String title;

    @Schema(description = "Job description", example = "Looking for experienced Java developer with 5+ years experience")
    private String description;

    @Schema(description = "Job location", example = "Chennai")
    private String location;

    @Schema(description = "Salary amount", example = "1200000.0")
    private Double salary;

    @Schema(description = "Required skills", example = "Java, Spring Boot, MySQL")
    private String skills;

    @NotNull(message = "Expiry date is required")
    @Schema(description = "Job expiry date", example = "2024-12-31", required = true)
    private LocalDate expiryDate;
}