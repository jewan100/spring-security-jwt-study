package com.auth.study.auth.jwt;

import java.io.IOException;
import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.study.auth.config.PublicEndpoint;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 인증을 처리하는 필터
 * - 매 요청마다 한 번 실행 (OncePerRequestFilter).
 * - Access Token이 존재하고 유효하면 SecurityContext에 인증 정보를 저장
 * - Public Endpoint인 경우 필터를 적용하지 않음
 */
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;

	/**
	 * 요청마다 호출되며 JWT를 추출하고 검증해 인증 정보를 SecurityContext에 설정
	 *
	 * @param request     HTTP 요청
	 * @param response    HTTP 응답
	 * @param filterChain 필터 체인
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		// Authorization 헤더에서 JWT 추출 → Claims 추출 → Access Token 여부 확인 → 인증 객체 생성
		extractJwtFromRequest(request)
	        .map(jwtUtils::extractClaims)
	        .filter(jwtUtils::isAccessToken)
	        .map(jwtUtils::getAuthentication)
	        .ifPresent(SecurityContextHolder.getContext()::setAuthentication);

		// 다음 필터로 요청 전달
		filterChain.doFilter(request, response);
	}

	/**
	 * 이 요청에 대해 필터를 적용하지 않아야 하는지 여부를 판단
	 * PublicEndpoint에 해당하는 URI는 필터를 건너뜀
	 *
	 * @param request HTTP 요청
	 * @return true: 필터 제외, false: 필터 적용
	 */
	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
		return PublicEndpoint.matches(request.getRequestURI());
	}

	/**
	 * Authorization 헤더에서 JWT를 추출
	 *
	 * @param request HTTP 요청
	 * @return "Bearer " 접두어 제거 후 JWT 문자열을 Optional로 감싸서 반환
	 */
	private Optional<String> extractJwtFromRequest(HttpServletRequest request) {
		return Optional.ofNullable(request.getHeader(JwtConstants.AUTHORIZATION_HEADER.getValue()))
				.filter(header -> header.startsWith(JwtConstants.BEARER_PREFIX.getValue()))
				.map(header -> header.substring((JwtConstants.BEARER_PREFIX.getValue().length())));
	}
}
