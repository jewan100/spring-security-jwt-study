package com.auth.study.auth.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security에서 인가 실패(403 Forbidden) 시 호출되는 핸들러
 * - 인증은 되었지만 접근 권한이 없을 때 발생
 */
@Component
@RequiredArgsConstructor
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

	/** JSON 직렬화를 위한 ObjectMapper */
	private final ObjectMapper objectMapper;

	/**
	 * 인가 실패 시 호출되는 메서드
	 * - 응답 본문에 JSON 형태로 실패 응답을 작성
	 *
	 * @param request       클라이언트의 HTTP 요청
	 * @param response      서버의 HTTP 응답
	 * @param accessDeniedException 발생한 인가 예외
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response,
			AccessDeniedException accessDeniedException) throws IOException, ServletException {
		ErrorCode errorCode = ErrorCode.FORBIDDEN;

		// 응답 인코딩 및 타입 설정
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(errorCode.getStatus().value());

		// 에러 정보를 JSON 형식으로 응답
		objectMapper.writeValue(response.getWriter(), ApiResponse.fail(errorCode));
	}

}