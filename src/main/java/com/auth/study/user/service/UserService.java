package com.auth.study.user.service;

import java.util.UUID;

import com.auth.study.user.domain.User;
import com.auth.study.user.dto.request.UserCreateRequest;

public interface UserService {

	void createUser(UserCreateRequest request);

	User getUser(UUID id);
}
