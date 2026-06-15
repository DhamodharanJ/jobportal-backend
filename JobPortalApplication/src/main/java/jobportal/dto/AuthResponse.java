package jobportal.dto;

import jobportal.model.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Authentication response with JWT token")
public class AuthResponse {

    @Schema(description = "JWT authentication token")
    private String token;

    @Schema(description = "Email of authenticated user", example = "dhamo@example.com")
    private String email;

    @Schema(description = "Role of authenticated user", example = "JOB_SEEKER")
    private Role role;
}