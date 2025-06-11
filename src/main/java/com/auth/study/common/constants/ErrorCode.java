package com.auth.study.common.constants;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

	// COMMON
	NOT_FOUND(HttpStatus.NOT_FOUND, "리소스를 찾을 수 없어요."),
	FORBIDDEN(HttpStatus.FORBIDDEN, "접근 권한이 없어요."),
	UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "인증이 필요해요."),
	BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청이에요."),
	SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했어요."),
	
	// USER
	EMAIL_ALREADY_EXISTS(HttpStatus.CONFLICT, "이미 존재하는 이메일이에요."),
	USER_DELETED(HttpStatus.NOT_FOUND, "탈퇴한 사용자예요."),
	
	// AUTH
	USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없어요."),
	INVALID_PASSWORD(HttpStatus.BAD_REQUEST, "잘못된 비밀번호예요."),
	
	// JWT
	UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST, "지원하지 않는 토큰 형식이에요."),
	EMPTY_TOKEN(HttpStatus.BAD_REQUEST, "토큰이 없거나 잘못된 형식이에요."),
	EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었어요."),
	INVALID_TOKEN_TYPE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 타입이에요."),
	INVALID_TOKEN_SIGNATURE(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰 서명이에요."),
	REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "서버에 저장된 리프레시 토큰이 없어요."),
	INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 리프레시 토큰이에요.");

	private final HttpStatus status;
	private final String message;
}