package com.agn1kobi.e_commerce_backend.user;

import com.agn1kobi.e_commerce_backend.config.JwtService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public record RegisterRequest(
            @NotBlank String username,
            @Email String email,
            @NotBlank String password,
            @NotBlank String role
    ) {}

    public record RegisterResponse(String id, String email, String role) {}
    public record MessageResponse(String message) {}
    public record TokenResponse(String token, String tokenType) {}

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
        Role role = Role.valueOf(request.role().toUpperCase());
        UserEntity entity = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();
        UserEntity saved = userRepository.save(entity);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(new RegisterResponse(saved.getId().toString(), saved.getEmail(), saved.getRole().name()));
    }

    @GetMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestParam @Email String email, @RequestParam String password) {
        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        if (auth.isAuthenticated()) {
            String token = jwtService.generateToken(email, Map.of("role", auth.getAuthorities().iterator().next().getAuthority()));
            return ResponseEntity.ok(new TokenResponse(token, "Bearer"));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}
