package com.auth.study.auth.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.auth.study.auth.dto.request.LoginRequest;
import com.auth.study.auth.dto.response.LoginResponse;
import com.auth.study.auth.dto.response.RefreshResponse;
import com.auth.study.auth.jwt.Jwt;
import com.auth.study.auth.mapper.AuthMapper;
import com.auth.study.auth.service.AuthService;
import com.auth.study.common.dto.response.ApiResponse;

import lombok.RequiredArgsConstructor;

/**
 * 인증 관련 요청을 처리하는 REST 컨트롤러
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

	private final AuthService authService;
	private final AuthMapper authMapper;

	/**
	 * 로그인 요청을 처리
	 * - 사용자 정보를 검증하고 Access/Refresh Token을 발급
	 *
	 * @param request 로그인 요청 정보
	 * @return JWT 토큰을 포함한 응답
	 */
	@PostMapping("/login")
	public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody LoginRequest request) {
		Jwt token = authService.login(request);
		return ResponseEntity.ok(ApiResponse.success(authMapper.toLoginResponse(token)));
	}

	/**
	 * 로그아웃 요청을 처리
	 *
	 * @param request Authorization 헤더에 포함된 Access Token
	 * @return 성공 응답 (내용 없음)
	 */
	@PostMapping("/logout")
	public ResponseEntity<ApiResponse<Void>> logout(@RequestHeader("Authorization") String request) {
		authService.logout(request);
		return ResponseEntity.ok(ApiResponse.success());
	}

	/**
	 * Access Token 재발급 요청을 처리
	 * - 유효한 Refresh Token이 필요
	 *
	 * @param request Authorization 헤더에 포함된 Refresh Token
	 * @return 새롭게 발급된 Access Token 응답
	 */
	@PostMapping("/refresh")
	public ResponseEntity<ApiResponse<RefreshResponse>> refresh(@RequestHeader("Authorization") String request) {
		Jwt token = authService.reissueAccessToken(request);
		return ResponseEntity.ok(ApiResponse.success(authMapper.toRefreshResponse(token)));
	}
}