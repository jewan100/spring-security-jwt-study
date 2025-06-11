package com.auth.study.common.exception;

import com.auth.study.common.constants.ErrorCode;

import lombok.Getter;

@Getter
public class ApiException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;

	public ApiException(ErrorCode errorCode) {
		super(errorCode.getMessage());
		this.errorCode = errorCode;
	}
}
