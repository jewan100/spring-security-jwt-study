package com.auth.study.auth.jwt;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.dto.response.ApiResponse;
import com.auth.study.common.exception.ApiException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * JWT 필터 처리 중 발생하는 예외(ApiException)를 감지하여
 * 일관된 JSON 형식의 에러 응답을 반환하는 예외 처리 필터
 */
@Component
@RequiredArgsConstructor
public class JwtExceptionFilter extends OncePerRequestFilter {

	/** JSON 직렬화를 위한 ObjectMapper */
	private final ObjectMapper objectMapper;

	/**
	 * 필터 체인 실행 중 ApiException이 발생하면 JSON 에러 응답을 반환합니다.
	 *
	 * @param request  클라이언트의 HTTP 요청
	 * @param response 서버의 HTTP 응답
	 * @param filterChain 필터 체인
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		try {
			filterChain.doFilter(request, response);
		} catch (ApiException e) {
			ErrorCode errorCode = e.getErrorCode();

			response.setCharacterEncoding(StandardCharsets.UTF_8.name());
			response.setContentType(MediaType.APPLICATION_JSON_VALUE);
			response.setStatus(errorCode.getStatus().value());

			objectMapper.writeValue(response.getWriter(), ApiResponse.fail(errorCode));
		}
	}
	
}
