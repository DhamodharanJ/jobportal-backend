package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "recruiters")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Recruiter profile information")
public class Recruiter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the recruiter", example = "1")
    private Long recruiterId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    @Schema(description = "Associated user account")
    private User user;

    @Column(nullable = false)
    @Schema(description = "Company name", example = "TVM INFO Tech", required = true)
    private String companyName;

    @Schema(description = "Company website URL", example = "https://tvm.com")
    private String companyWebsite;

    @Column(nullable = false)
    @Schema(description = "Contact phone number", example = "9123456789", required = true)
    private String phone;
}