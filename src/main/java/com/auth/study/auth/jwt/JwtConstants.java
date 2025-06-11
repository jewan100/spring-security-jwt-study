package com.auth.study.auth.jwt;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtConstants {
	
	// Header
	AUTHORIZATION_HEADER("Authorization"),
	
	// Prefix
	BEARER_PREFIX("Bearer "),
	
	// Claims
	CLAIM_TYPE("type"),
	CLAIM_NAME("name"),
	
	// Token type
	TOKEN_TYPE_ACCESS("access"),
	TOKEN_TYPE_REFRESH("refresh");
		
	private final String value;

}