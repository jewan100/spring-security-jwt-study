package com.auth.study.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.auth.study.user.entity.UserEntity;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {

	boolean existsByEmail(String email);
	
	Optional<UserEntity> findByEmail(String email);
}
