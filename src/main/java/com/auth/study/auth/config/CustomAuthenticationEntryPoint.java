package com.auth.study.auth.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

/**
 * 인증 실패(401 Unauthorized) 시 동작하는 진입점(EntryPoint) 구현체
 * - 인증이 필요한 자원에 인증되지 않은 사용자가 접근할 경우 호출
 */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

	/** JSON 직렬화를 위한 ObjectMapper */
	private final ObjectMapper objectMapper;

	/**
	 * 인증이 되지 않은 사용자가 보호된 리소스에 접근할 경우 호출
	 * - 응답 본문에 JSON 형식의 실패 응답을 작성
	 *
	 * @param request       클라이언트의 HTTP 요청
	 * @param response      서버의 HTTP 응답
	 * @param authException 발생한 인증 예외
	 * @throws IOException
	 * @throws ServletException
	 */
	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException authException) throws IOException, ServletException {
		ErrorCode errorCode = ErrorCode.UNAUTHORIZED;

		// 응답 인코딩 및 타입 설정
		response.setCharacterEncoding(StandardCharsets.UTF_8.name());
		response.setContentType(MediaType.APPLICATION_JSON_VALUE);
		response.setStatus(errorCode.getStatus().value());
		
		// 에러 정보를 JSON 형식으로 응답
		objectMapper.writeValue(response.getWriter(), ApiResponse.fail(errorCode));
	}

}