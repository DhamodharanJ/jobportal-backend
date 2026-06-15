package jobportal.repository;

import jobportal.model.Application;
import jobportal.model.Job;
import jobportal.model.JobSeeker;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Long> {
    Page<Application> findByJobSeeker(JobSeeker jobSeeker, Pageable pageable);
    Page<Application> findByJob(Job job, Pageable pageable);
    boolean existsByJobAndJobSeeker(Job job, JobSeeker jobSeeker);
}