package com.auth.study.auth.config;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

/**
 * 인증이 필요 없는 공개 API 엔드포인트를 정의하는 enum
 * - JwtAuthenticationFilter에서 필터링 제외 대상으로 사용
 */
@RequiredArgsConstructor
public enum PublicEndpoint {

	// USER
	USER_CREATE("/api/v1/user/create"),

	// AUTH
	AUTH_LOGIN("/api/v1/auth/login"),
	AUTH_REFRESH("/api/v1/auth/refresh");

	private final String path;

	/**
	 * 요청 경로가 Public Endpoint에 해당하는지 검사
	 *
	 * @param requestPath 요청 URI
	 * @return true: 인증 없이 접근 가능한 URI, false: 인증 필요
	 */
	public static boolean matches(String requestPath) {
		return Arrays.stream(values())
				.anyMatch(endpoint -> endpoint.path.equals(requestPath));
	}
	
	/**
	 * 모든 Public Endpoint 경로 목록을 반환
	 *
	 * @return 인증 없이 접근 가능한 URI 배열
	 */
	public static String[] getAllPaths() {
        return Arrays.stream(values())
                .map(endpoint -> endpoint.path)
                .toArray(String[]::new);
    }
}
