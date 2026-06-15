package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "applications")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Job application information")
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the application", example = "1")
    private Long applicationId;

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    @Schema(description = "Job being applied for")
    private Job job;

    @ManyToOne
    @JoinColumn(name = "seeker_id", nullable = false)
    @Schema(description = "Job seeker who applied")
    private JobSeeker jobSeeker;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Current status of the application", example = "APPLIED", required = true)
    private ApplicationStatus status;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when application was submitted")
    private LocalDateTime appliedDate;

    @Schema(description = "Timestamp when application was last updated")
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() {
        appliedDate = LocalDateTime.now();
        updatedDate = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedDate = LocalDateTime.now();
    }
}