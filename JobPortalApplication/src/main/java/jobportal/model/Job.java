package jobportal.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "jobs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Job posting information")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Unique identifier of the job", example = "1")
    private Long jobId;

    @ManyToOne
    @JoinColumn(name = "recruiter_id", nullable = false)
    @Schema(description = "Recruiter who posted this job")
    private Recruiter recruiter;

    @Column(nullable = false)
    @Schema(description = "Job title", example = "Senior Java Developer", required = true)
    private String title;

    @Column(length = 2000)
    @Schema(description = "Detailed job description", example = "Looking for experienced Java developer...")
    private String description;

    @Schema(description = "Job location", example = "Chennai")
    private String location;

    @Schema(description = "Salary offered", example = "1200000.0")
    private Double salary;

    @Schema(description = "Required skills", example = "Java, Spring, Microservices")
    private String skills;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Schema(description = "Current status of the job", example = "ACTIVE", required = true)
    private JobStatus status;

    @Column(nullable = false)
    @Schema(description = "Date when job was posted", required = true)
    private LocalDate postedDate;

    @Schema(description = "Job expiry date", example = "2024-12-31")
    private LocalDate expiryDate;

    @Column(nullable = false, updatable = false)
    @Schema(description = "Timestamp when job was created")
    private LocalDateTime createdAt;

    @Schema(description = "Timestamp when job was last updated")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (postedDate == null) {
            postedDate = LocalDate.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}