package com.auth.study.auth.repository;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

import com.auth.study.auth.jwt.JwtProperties;

import lombok.RequiredArgsConstructor;

/**
 * Redis를 이용한 Refresh Token 저장소 구현체
 * - 사용자 ID(UUID)를 키로 사용하고, 토큰 만료 시간 설정은 JwtProperties에서 가져옴
 */
@Repository
@RequiredArgsConstructor
public class RefreshTokenRepositoryImpl implements RefreshTokenRepository {

	private final StringRedisTemplate redisTemplate;
	private final JwtProperties jwtProperties;

	/**
	 * Refresh Token을 Redis에 저장
	 *
	 * @param id 사용자 ID(UUID)
	 * @param refreshToken 저장할 Refresh Token
	 */
	@Override
	public void saveToken(UUID id, String refreshToken) {
		redisTemplate.opsForValue().set(id.toString(), refreshToken, jwtProperties.getRefreshExpiration(),
				TimeUnit.SECONDS);
	}

	/**
	 * Redis에서 사용자 ID로 저장된 Refresh Token을 조회
	 *
	 * @param id 사용자 ID(UUID)
	 * @return 저장된 Refresh Token (없으면 null)
	 */
	@Override
	public String getToken(UUID id) {
		return redisTemplate.opsForValue().get(id.toString());
	}

	/**
	 * Redis에서 Refresh Token을 삭제
	 *
	 * @param id 사용자 ID(UUID)
	 */
	@Override
	public void deleteToken(UUID id) {
		redisTemplate.delete(id.toString());
	}
}