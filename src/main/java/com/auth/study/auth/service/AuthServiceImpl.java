package com.auth.study.auth.service;

import java.util.UUID;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.auth.study.auth.dto.request.LoginRequest;
import com.auth.study.auth.jwt.Jwt;
import com.auth.study.auth.jwt.JwtProvider;
import com.auth.study.auth.jwt.JwtUtils;
import com.auth.study.auth.repository.RefreshTokenRepository;
import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.exception.ApiException;
import com.auth.study.user.domain.User;
import com.auth.study.user.mapper.UserMapper;
import com.auth.study.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

/**
 * 인증 관련 서비스 구현체
 * - 로그인, 로그아웃, Access Token 재발급 기능을 제공
 */
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private final UserRepository userRepository;
	private final RefreshTokenRepository refreshTokenRepository;
	
	private final UserMapper userMapper;
	private final PasswordEncoder passwordEncoder;
	private final JwtProvider jwtTokenProvider;
	private final JwtUtils jwtUtils;

	/**
	 * 로그인 요청을 처리
	 * 1. 이메일로 사용자 조회
	 * 2. 비밀번호 검증
	 * 3. Access/Refresh Token 발급
	 * 4. Refresh Token 저장
	 *
	 * @param request 로그인 요청 정보
	 * @return 발급된 JWT 토큰 (Access + Refresh)
	 */
	@Override
	public Jwt login(LoginRequest request) {
		User user = userRepository.findByEmail(request.email())
				.map(userMapper::toDomain)
				.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
		
		user.validatePassword(request.password(), passwordEncoder);

		Jwt token = jwtTokenProvider.generateTokens(user);
		refreshTokenRepository.saveToken(user.getId(), token.getRefreshToken());

		return token;
	}

	/**
	 * 로그아웃 요청을 처리
	 * - 토큰에서 사용자 ID를 추출하여 Redis에서 Refresh Token 삭제
	 *
	 * @param request Authorization 헤더 값 (Bearer 포함)
	 */
	@Override
	public void logout(String request) {
		String token = jwtUtils.extractToken(request);
		UUID id = UUID.fromString(jwtUtils.extractSubject(token));
		refreshTokenRepository.deleteToken(id);
	}

	/**
	 * Refresh Token을 검증하고 Access Token을 재발급
	 * - 요청된 Refresh Token과 Redis에 저장된 값이 일치하는지 확인
	 * - 일치할 경우 Access Token 재발급
	 *
	 * @param request Authorization 헤더 값 (Bearer 포함)
	 * @return 새로 발급된 Access Token (Refresh Token은 그대로)
	 */
	@Override
	public Jwt reissueAccessToken(String request) {
		String refreshToken = jwtUtils.extractToken(request);
		UUID id = UUID.fromString(jwtUtils.extractSubject(refreshToken));
		
		String storedRefreshToken = refreshTokenRepository.getToken(id);
		
		validateRefreshToken(refreshToken, storedRefreshToken);
		
		User user = userRepository.findById(id)
				.map(userMapper::toDomain)
				.orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
		String accessToken = jwtTokenProvider.generateAccessToken(user);
		
		return Jwt.builder()
				.accessToken(accessToken)
				.build();
	}
	
	/**
	 * Refresh Token의 유효성을 검증
	 * - Redis에 저장된 토큰이 없거나 일치하지 않으면 예외 발생
	 *
	 * @param refreshToken 클라이언트가 보낸 Refresh Token
	 * @param storedRefreshToken Redis에 저장된 Refresh Token
	 */
	private void validateRefreshToken(String refreshToken, String storedRefreshToken) {
		if (storedRefreshToken == null) {
			throw new ApiException(ErrorCode.REFRESH_TOKEN_NOT_FOUND);
		}
		if (!storedRefreshToken.equals(refreshToken)) {
			throw new ApiException(ErrorCode.INVALID_REFRESH_TOKEN);
		}
	}
}
