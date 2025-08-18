package com.agn1kobi.e_commerce_backend.user;

import com.agn1kobi.e_commerce_backend.user.model.UserEntity;
import com.agn1kobi.e_commerce_backend.user.repository.UserRepository;
import com.agn1kobi.e_commerce_backend.user.service.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceImplTest {

    @Mock
    private UserRepository userRepository;


    @Test
    void loadsUserByEmail() {
        var svc = new UserDetailsServiceImpl(userRepository);
        UserEntity user = new UserEntity();
        user.setEmail("user@example.com");

        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));


        var ud = svc.loadUserByUsername("user@example.com");
        assertThat(ud).isNotNull();
    }

    @Test
    void throwsWhenNotFound() {
        var svc = new UserDetailsServiceImpl(userRepository);
        when(userRepository.findByEmail("missing@example.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> svc.loadUserByUsername("missing@example.com"))
                .isInstanceOf(UsernameNotFoundException.class);
    }
}
