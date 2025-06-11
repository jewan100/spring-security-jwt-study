package com.auth.study.auth.dto.response;

public record LoginResponse(
	String accessToken,
	String refreshToken
) {

}
