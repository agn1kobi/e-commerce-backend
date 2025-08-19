package com.agn1kobi.e_commerce_backend.user.api;

import com.agn1kobi.e_commerce_backend.common.types.RequestResult;
import com.agn1kobi.e_commerce_backend.user.dtos.LoginUserResponseDto;
import com.agn1kobi.e_commerce_backend.user.dtos.RegisterUserRequestDto;
import com.agn1kobi.e_commerce_backend.user.dtos.RegisterUserResponseDto;
import com.agn1kobi.e_commerce_backend.user.service.UserDetailsServiceImpl;
import com.agn1kobi.e_commerce_backend.user.service.UserService;
import com.agn1kobi.e_commerce_backend.user.service.UserServiceImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserDetailsServiceImpl userDetailsService;
    private final UserServiceImpl userService;


    @PostMapping("/register")
    public ResponseEntity<RegisterUserResponseDto> register(@Valid @RequestBody RegisterUserRequestDto request) {
        RequestResult<RegisterUserResponseDto> result = userService.registerUser(request);

        return switch (result.result()) {
            case FAILURE ->  ResponseEntity.unprocessableEntity().build();
            case SUCCESS ->  ResponseEntity.ok().body(result.response());
        };
    }

    @GetMapping("/login")
    public ResponseEntity<LoginUserResponseDto> login(@RequestParam @Email String email, @RequestParam String password) {

        RequestResult<LoginUserResponseDto> result = userService.loginUser(email, password);

        return switch (result.result()) {
            case FAILURE -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
            case SUCCESS ->  ResponseEntity.ok().body(result.response());
        };
    }
}
