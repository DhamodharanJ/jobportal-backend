package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "job_seekers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Job seeker profile information")
public class JobSeeker {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the job seeker", example = "1")
    private Long seekerId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Associated user account")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Full name of the job seeker", example = "dhamodharan", required = true)
    private String fullName;

    @Column(nullable = false)
    @Schema(description = "Contact phone number", example = "9876543210", required = true)
    private String phone;

    @Column(length = 1000)
    @Schema(description = "Skills possessed by the job seeker", example = "Java, Spring Boot, MySQL")
    private String skills;

    @Schema(description = "Years of experience", example = "3")
    private Integer experience;
}