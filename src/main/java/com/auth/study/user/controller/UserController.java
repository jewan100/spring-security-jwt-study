package com.auth.study.user.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.auth.study.common.constants.SuccessCode;
import com.auth.study.common.dto.response.ApiResponse;
import com.auth.study.user.domain.User;
import com.auth.study.user.dto.request.UserCreateRequest;
import com.auth.study.user.dto.response.UserGetResponse;
import com.auth.study.user.mapper.UserMapper;
import com.auth.study.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserMapper userMapper;

	@PostMapping("/create")
	public ResponseEntity<ApiResponse<Void>> createUser(@RequestBody UserCreateRequest request) {
		userService.createUser(request);
		return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(SuccessCode.SUCCESS_CREATED));
	}

	@GetMapping("/me")
	public ResponseEntity<ApiResponse<UserGetResponse>> getUser(@AuthenticationPrincipal UUID id) {
		User user = userService.getUser(id);
		return ResponseEntity.ok().body(ApiResponse.success(userMapper.toResponse(user)));
	}
}
