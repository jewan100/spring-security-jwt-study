package com.auth.study.user.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.exception.ApiException;
import com.auth.study.user.domain.User;
import com.auth.study.user.dto.request.UserCreateRequest;
import com.auth.study.user.mapper.UserMapper;
import com.auth.study.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;

	@Override
	public void createUser(UserCreateRequest request) {
		validateDuplicateEmail(request.email());

		User user = userMapper.toDomain(request);
		user.applyEncodedPassword(passwordEncoder.encode(user.getPassword()));

		userRepository.save(userMapper.toEntity(user));
	}

	@Override
	public User getUser(UUID id) {
		return userRepository.findById(id)
				.map(userMapper::toDomain)
				.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
	}

	private void validateDuplicateEmail(String email) {
		if (userRepository.existsByEmail(email)) {
			throw new ApiException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}
}
