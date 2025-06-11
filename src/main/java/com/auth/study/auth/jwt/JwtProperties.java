package com.auth.study.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

/**
 * application.yml에 정의된
 * JWT 관련 설정 값을 매핑하는 설정 클래스
 *
 * prefix가 'jwt'이므로, 'jwt.secret-key'와 같은 형태로 값을 설정할 수 있습니다.
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {
	
	 /** JWT 서명을 위한 비밀 키 (HS256 등 알고리즘에서 사용) */
	private String secretKey;
	
	/** Access Token의 만료 시간 (밀리초 단위) */
	private long accessExpiration;
	
	 /** Refresh Token의 만료 시간 (밀리초 단위) */
	private long refreshExpiration;
}