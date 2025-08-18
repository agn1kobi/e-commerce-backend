package com.agn1kobi.e_commerce_backend.config;

import com.agn1kobi.e_commerce_backend.config.service.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class JwtServiceTest {

    @Test
    void generatesAndValidatesToken() {
    JwtService jwt = new JwtService("testsecret-testsecret-testsecret-testsecret", 3600);
    String token = jwt.generateToken("user@example.com", java.util.Collections.emptyMap());
    assertThat(jwt.isTokenValid(token)).isTrue();
    assertThat(jwt.extractSubject(token)).isEqualTo("user@example.com");
    }
}
