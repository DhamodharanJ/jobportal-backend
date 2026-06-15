package jobportal.service;

import jobportal.dto.AuthRequest;
import jobportal.dto.AuthResponse;
import jobportal.dto.RegisterRequest;
import jobportal.exception.CustomException;
import jobportal.model.*;
import jobportal.repository.JobSeekerRepository;
import jobportal.repository.RecruiterRepository;
import jobportal.repository.UserRepository;
import jobportal.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final JobSeekerRepository jobSeekerRepository;
    private final RecruiterRepository recruiterRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;

    public AuthService(UserRepository userRepository, JobSeekerRepository jobSeekerRepository,
                       RecruiterRepository recruiterRepository, PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil, AuthenticationManager authenticationManager,
                       CustomUserDetailsService userDetailsService) {
        this.userRepository = userRepository;
        this.jobSeekerRepository = jobSeekerRepository;
        this.recruiterRepository = recruiterRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
    }

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        log.info("Registration attempt for email: {}", request.getEmail());


        if (userRepository.existsByEmail(request.getEmail())) {
            log.error("Registration failed: Email already exists - {}", request.getEmail());
            throw new CustomException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole());

        user = userRepository.save(user);
        log.info("User created successfully with email: {}", user.getEmail());

        if (request.getRole() == Role.JOB_SEEKER) {
            if (request.getFullName() == null || request.getPhone() == null) {
                throw new CustomException("Full name and phone are required for job seekers");
            }
            JobSeeker jobSeeker = new JobSeeker();
            jobSeeker.setUser(user);
            jobSeeker.setFullName(request.getFullName());
            jobSeeker.setPhone(request.getPhone());
            jobSeeker.setSkills(request.getSkills());
            jobSeeker.setExperience(request.getExperience());
            jobSeekerRepository.save(jobSeeker);
            log.info("Job seeker profile created for: {}", user.getEmail());
        } else if (request.getRole() == Role.RECRUITER) {
            if (request.getCompanyName() == null || request.getPhone() == null) {
                throw new CustomException("Company name and phone are required for recruiters");
            }
            Recruiter recruiter = new Recruiter();
            recruiter.setUser(user);
            recruiter.setCompanyName(request.getCompanyName());
            recruiter.setCompanyWebsite(request.getCompanyWebsite());
            recruiter.setPhone(request.getPhone());
            recruiterRepository.save(recruiter);
            log.info("Recruiter profile created for: {}", user.getEmail());
        }

        String token = jwtUtil.generateToken(user.getEmail());

        log.info("Registration completed successfully for: {}", user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }

    public AuthResponse login(AuthRequest request) {
        log.info("Login attempt for email: {}", request.getEmail());

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (Exception e) {
            log.error("Authentication failed for email: {}", request.getEmail());
            throw new CustomException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new CustomException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail());

        log.info("Login successful for email: {}", user.getEmail());
        return new AuthResponse(token, user.getEmail(), user.getRole());
    }
}