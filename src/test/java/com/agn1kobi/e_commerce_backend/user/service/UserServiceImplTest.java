package com.agn1kobi.e_commerce_backend.user.service;

import com.agn1kobi.e_commerce_backend.config.service.JwtService;
import com.agn1kobi.e_commerce_backend.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    UserRepository userRepository;

    @MockitoBean
    PasswordEncoder passwordEncoder;

    @InjectMocks
    JwtService jwtService;

    @MockitoBean
    AuthenticationManager authenticationManager;

    UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        userService = new UserServiceImpl(userRepository, passwordEncoder, jwtService, authenticationManager);
    }
    
}
