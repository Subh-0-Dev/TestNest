package com.subh.TestNest.Service;

import com.subh.TestNest.Dto.RegisterRequest;
import com.subh.TestNest.Dto.login.LoginRequest;
import com.subh.TestNest.Dto.login.LoginResponse;
import com.subh.TestNest.Entity.Role;
import com.subh.TestNest.Entity.University;
import com.subh.TestNest.Entity.User;
import com.subh.TestNest.Repository.UniversityRepository;
import com.subh.TestNest.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private UniversityRepository universityRepository;

    public User register(RegisterRequest request) {

            if (request.getEmail() == null || request.getPassword() == null) {
                throw new IllegalArgumentException("Email and password are required");
            }


            if (userRepository.existsByEmail(request.getEmail())) {
                throw new IllegalArgumentException("Email already in use");
            }


        User user=new User();
            user.setFullName(request.getFullName());
            user.setEmail(request.getEmail());
            user.setPassword(passwordEncoder.encode(request.getPassword()));
            user.setRole(request.getRole());
        if (request.getRole() == Role.STUDENT || request.getRole() == Role.FACULTY) {
            if (request.getUniversityName() == null || request.getUniversityName().isBlank()) {
                throw new IllegalArgumentException("University name is required for " + request.getRole());
            }

            Optional<University> universityOpt = universityRepository.findByUniversityName(request.getUniversityName());
            if (universityOpt.isEmpty()) {
                throw new IllegalArgumentException("University does not exist in the database");
            }

            user.setUniversity(universityOpt.get());
        } else if (request.getRole() == Role.ADMIN) {
            // Ensure no university is set for admin
            if (request.getUniversityName() != null) {
                throw new IllegalArgumentException("Admin should not be assigned to any university");
            }
            user.setUniversity(null);
        }
        return userRepository.save(user);
    }

    public LoginResponse login(LoginRequest request) {
        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            String accessToken = jwtUtils.generateToken(userDetails);
            String refreshToken = jwtUtils.generateRefreshToken(userDetails);

            LoginResponse response = new LoginResponse();
            response.setAccessToken(accessToken);
            response.setRefreshToken(refreshToken);
            response.setRole(userDetails.getRole().name());

            return response;

        } catch (BadCredentialsException e) {
            throw new IllegalArgumentException("Invalid email or password");
        }
    }
}
