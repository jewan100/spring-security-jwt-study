package com.auth.study.auth.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants.ComponentModel;
import org.mapstruct.ReportingPolicy;

import com.auth.study.auth.dto.response.LoginResponse;
import com.auth.study.auth.dto.response.RefreshResponse;
import com.auth.study.auth.jwt.Jwt;

@Mapper(
    componentModel = ComponentModel.SPRING,
    unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface AuthMapper {
	LoginResponse toLoginResponse(Jwt doamin);
	RefreshResponse toRefreshResponse(Jwt domain);
}
