package com.auth.study.auth.jwt;

import java.util.UUID;

import javax.crypto.SecretKey;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.exception.ApiException;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

/**
 * JWT 관련 유틸리티 클래스
 * - 토큰 추출, 검증, 파싱, 인증 객체 생성 등의 기능을 제공
 */
@Component
@RequiredArgsConstructor
public class JwtUtils {

	/** application.yml에 정의된 JWT 설정 정보 */
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
	 * Authorization 헤더에서 실제 토큰 값만 추출
	 *
	 * @param header HTTP Authorization 헤더 값
	 * @return "Bearer " 접두어를 제외한 순수 토큰 문자열
	 * @throws ApiException 토큰이 없거나 형식이 잘못된 경우
	 */
	public String extractToken(String header) {
		if (header == null || !header.startsWith(JwtConstants.BEARER_PREFIX.getValue())) {
			throw new ApiException(ErrorCode.EMPTY_TOKEN);
		}
		return header.substring(JwtConstants.BEARER_PREFIX.getValue().length());
	}

	/**
	 * JWT 토큰에서 Claims(페이로드 데이터)를 추출
	 *
	 * @param token JWT 문자열
	 * @return Claims 객체 (subject, custom claims 등 포함)
	 * @throws ApiException 서명 오류, 만료, 형식 오류 등 다양한 예외에 대응
	 */
	public Claims extractClaims(String token) {
		try {
			return Jwts.parser()
				.verifyWith(key) // 시크릿 키로 서명 검증
				.build()
				.parseSignedClaims(token)
				.getPayload(); // 페이로드(Claims) 추출
		} catch (SecurityException | MalformedJwtException e) {
			throw new ApiException(ErrorCode.INVALID_TOKEN_SIGNATURE);
		} catch (ExpiredJwtException e) {
			throw new ApiException(ErrorCode.EXPIRED_TOKEN);
		} catch (UnsupportedJwtException e) {
			throw new ApiException(ErrorCode.UNSUPPORTED_TOKEN);
		} catch (IllegalArgumentException e) {
			throw new ApiException(ErrorCode.EMPTY_TOKEN);
		}
	}
	
	/**
	 * 토큰에서 subject를 추출합니다.
	 *
	 * @param token JWT 문자열
	 * @return subject 값
	 */
	public String extractSubject(String token) {
		Claims claims = extractClaims(token);
		return claims.getSubject();
	}
	
	/**
	 * 주어진 Claims가 Access Token인지 확인합니다.
	 *
	 * @param claims JWT Claims
	 * @return Access Token이면 true, 아니면 false
	 */
	public boolean isAccessToken(Claims claims) {
		return claims.get(JwtConstants.CLAIM_TYPE.getValue())
				.equals(JwtConstants.TOKEN_TYPE_ACCESS.getValue());
	}

	/**
	 * JWT Claims로부터 Spring Security의 Authentication 객체를 생성합니다.
	 *
	 * @param claims JWT Claims
	 * @return 인증 객체 (사용자 ID만 포함된 상태)
	 */
	public Authentication getAuthentication(Claims claims) {
		UUID id = UUID.fromString(claims.getSubject());
		return new UsernamePasswordAuthenticationToken(id, "");
	}
	
}
