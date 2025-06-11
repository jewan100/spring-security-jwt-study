package com.auth.study.user.dto.request;

public record UserCreateRequest(
	String email,
	String password,
	String name
) {

}
