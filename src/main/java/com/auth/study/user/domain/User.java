package com.auth.study.user.domain;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.exception.ApiException;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class User {

	private UUID id;
	private String email;
	private String password;
	private String name;

	public void applyEncodedPassword(String encodedPassword) {
		password = encodedPassword;
	}

	public User validatePassword(String rawPassword, PasswordEncoder passwordEncoder) {
		if (!passwordEncoder.matches(rawPassword, password)) {
			throw new ApiException(ErrorCode.INVALID_PASSWORD);
		}
		return this;
	}
}