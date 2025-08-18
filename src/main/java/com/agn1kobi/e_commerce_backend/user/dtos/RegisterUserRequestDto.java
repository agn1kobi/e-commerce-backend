package com.agn1kobi.e_commerce_backend.user.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterUserRequestDto(
	@NotBlank String username,
	@Email String email,
	@NotBlank String password,
	@NotBlank String role
) {}
