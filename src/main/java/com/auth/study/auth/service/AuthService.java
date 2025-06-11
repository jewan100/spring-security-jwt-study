package com.auth.study.auth.service;

import com.auth.study.auth.dto.request.LoginRequest;
import com.auth.study.auth.jwt.Jwt;

public interface AuthService {

	Jwt login(LoginRequest request);

	void logout(String request);

	Jwt reissueAccessToken(String request);

}