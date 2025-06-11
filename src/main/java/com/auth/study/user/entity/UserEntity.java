package com.auth.study.user.entity;

import java.util.UUID;

import org.hibernate.annotations.UuidGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class UserEntity {

	@Id
	@UuidGenerator
	@Column(columnDefinition = "BINARY(16)")
	private UUID id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String password;

	@Setter
	@Column(nullable = false)
	private String name;

	@Builder
	public UserEntity(String email, String password, String name) {
		this.email = email;
		this.password = password;
		this.name = name;
	}

}