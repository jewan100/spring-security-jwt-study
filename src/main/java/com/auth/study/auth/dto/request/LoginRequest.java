package com.auth.study.auth.dto.request;

public record LoginRequest(
	String email,
	String password
) {
	
}