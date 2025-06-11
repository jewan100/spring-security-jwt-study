package com.auth.study.auth.repository;

import java.util.UUID;

public interface RefreshTokenRepository {

	void saveToken(UUID id, String refreshToken);

	String getToken(UUID id);

	void deleteToken(UUID id);

}