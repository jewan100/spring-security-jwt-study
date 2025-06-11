package com.auth.study.auth.jwt;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Jwt {

	private String accessToken;
	private String refreshToken;
}
