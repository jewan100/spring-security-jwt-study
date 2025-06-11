package com.auth.study.auth.jwt;

import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

import com.auth.study.user.domain.User;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * JWT 생성과 관련된 로직을 담당하는 클래스
 * - Access Token, Refresh Token을 생성
 * - 비밀 키를 초기화하고 서명용 키 객체를 준비
 */
@Component
@RequiredArgsConstructor
public class JwtProvider {

	/** application.yml에 정의된 JWT 관련 설정을 주입 */
	private final JwtProperties jwtProperties;

	/** 서명 검증에 사용될 HMAC 비밀 키 */
	private SecretKey key;

	/**
	 * 객체 생성 후 호출되는 초기화 메서드
	 * BASE64로 인코딩된 시크릿 키를 디코딩하여 HMAC 키 객체로 변환
	 */
	@PostConstruct
	public void init() {
		byte[] keyBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
		this.key = Keys.hmacShaKeyFor(keyBytes);
	}
	
	/**
	 * Access Token과 Refresh Token을 함께 생성하여 Jwt 객체로 반환
	 *
	 * @param user 토큰의 주체가 되는 사용자
	 * @return Jwt 객체 (accessToken + refreshToken)
	 */
	public Jwt generateTokens(User user) {
		return Jwt.builder()
				.accessToken(generateAccessToken(user))
				.refreshToken(generateRefreshToken(user))
				.build();
	}

	/**
	 * Access Token을 생성
	 * - 유효 시간은 accessExpiration
	 * - 사용자 ID와 이름을 클레임에 담음
	 *
	 * @param user 토큰의 주체가 되는 사용자
	 * @return Access Token (JWT 문자열)
	 */
	public String generateAccessToken(User user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getAccessExpiration());

		return Jwts.builder()
				.subject(user.getId().toString())  // 사용자 ID
				.claim(JwtConstants.CLAIM_TYPE.getValue(), JwtConstants.TOKEN_TYPE_ACCESS.getValue()) // 토큰 타입: access
				.claim(JwtConstants.CLAIM_NAME.getValue(), user.getName()) // 사용자 이름
				.issuedAt(now) // 발급 시간
				.expiration(expiry) // 만료 시간
				.signWith(key) // 서명 키
				.compact(); // JWT 문자열 생성
	}

	/**
	 * Refresh Token을 생성
	 * - 유효 시간은 refreshExpiration
	 * - 사용자 ID만 클레임에 담음
	 *
	 * @param user 토큰의 주체가 되는 사용자
	 * @return Refresh Token (JWT 문자열)
	 */
	public String generateRefreshToken(User user) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + jwtProperties.getRefreshExpiration());

		return Jwts.builder()
				.subject(user.getId().toString()) // 사용자 ID
				.claim(JwtConstants.CLAIM_TYPE.getValue(), JwtConstants.TOKEN_TYPE_REFRESH.getValue()) // 토큰 타입: refresh
				.issuedAt(now) // 발급 시간
				.expiration(expiry) // 만료 시간
				.signWith(key) // 서명 키
				.compact(); // JWT 문자열 생성
	}
}
