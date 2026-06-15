package jobportal.repository;

import jobportal.model.Job;
import jobportal.model.JobStatus;
import jobportal.model.Recruiter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    Page<Job> findByStatus(JobStatus status, Pageable pageable);
    Page<Job> findByRecruiter(Recruiter recruiter, Pageable pageable);

    @Query("SELECT j FROM Job j WHERE j.status = 'ACTIVE' AND j.expiryDate < :today")
    List<Job> findExpiredJobs(LocalDate today);
}