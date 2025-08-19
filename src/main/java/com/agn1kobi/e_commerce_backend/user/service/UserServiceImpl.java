package com.agn1kobi.e_commerce_backend.user.service;

import com.agn1kobi.e_commerce_backend.common.types.RequestResult;
import com.agn1kobi.e_commerce_backend.common.types.Result;
import com.agn1kobi.e_commerce_backend.common.types.Role;
import com.agn1kobi.e_commerce_backend.config.service.JwtService;
import com.agn1kobi.e_commerce_backend.user.dtos.LoginUserResponseDto;
import com.agn1kobi.e_commerce_backend.user.dtos.RegisterUserRequestDto;
import com.agn1kobi.e_commerce_backend.user.dtos.RegisterUserResponseDto;
import com.agn1kobi.e_commerce_backend.user.model.UserEntity;
import com.agn1kobi.e_commerce_backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public RequestResult<RegisterUserResponseDto> registerUser(RegisterUserRequestDto request) {

        if (userRepository.existsByEmail(request.email())) {
            return new RequestResult<>(Result.FAILURE, null);
        }
        Role role = Role.valueOf(request.role().toUpperCase());
        UserEntity entity = UserEntity.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .role(role)
                .build();

        UserEntity saved = userRepository.save(entity);
        return new RequestResult<>(Result.SUCCESS, new RegisterUserResponseDto(saved.getId().toString(), saved.getEmail(), saved.getRole().name()));
    }

    public RequestResult<LoginUserResponseDto> loginUser(String email, String password) {

        Authentication auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

        if (auth.isAuthenticated()) {
            String token = jwtService.generateToken(email, Map.of("role", auth.getAuthorities().iterator().next().getAuthority()));
            return new RequestResult<>(Result.SUCCESS, new LoginUserResponseDto(token, "Bearer"));
        }

        return new RequestResult<>(Result.FAILURE, null);
    }
}
