package jobportal.dto;

import jobportal.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "User registration request")
public class RegisterRequest {

    @NotBlank(message = "Username is required")
    @Schema(description = "Display name", example = "dhamo")
    private String username;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(description = "Email address", example = "dhamo@gmail.com")
    private String email;

    @NotBlank(message = "Password is required")
    @Schema(description = "Account password", example = "password123")
    private String password;

    @NotNull(message = "Role is required")
    @Schema(description = "User role", example = "JOB_SEEKER")
    private Role role;

    @Schema(description = "Full name (for JOB_SEEKER)", example = "John Doe")
    private String fullName;

    @Schema(description = "Phone number", example = "9876543210")
    private String phone;

    @Schema(description = "Skills (for JOB_SEEKER)", example = "Java, Spring Boot")
    private String skills;

    @Schema(description = "Years of experience", example = "3")
    private Integer experience;

    @Schema(description = "Company name (for RECRUITER)", example = "Tech Solutions")
    private String companyName;

    @Schema(description = "Company website", example = "https://techsolutions.com")
    private String companyWebsite;
}