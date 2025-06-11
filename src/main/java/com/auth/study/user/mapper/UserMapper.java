package com.auth.study.user.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import com.auth.study.user.domain.User;
import com.auth.study.user.dto.request.UserCreateRequest;
import com.auth.study.user.dto.response.UserGetResponse;
import com.auth.study.user.entity.UserEntity;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface UserMapper {
	
	User toDomain(UserCreateRequest request);
	
	User toDomain(UserEntity entity);
	
	UserEntity toEntity(User domain);
	
	UserGetResponse toResponse(User domain);
}
