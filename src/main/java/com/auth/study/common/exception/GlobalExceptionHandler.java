package com.auth.study.common.exception;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.auth.study.common.constants.ErrorCode;
import com.auth.study.common.dto.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice // 모든 컨트롤러에서 발생하는 예외를 전역적으로 처리
// ResponseEntityExceptionHandler는 스프링 자체에서 발생하는 예외의 세부 처리
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Spring 내부에서 처리되는 예외를 감싸는 기본 핸들러
	 */
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception e, Object body, HttpHeaders headers,
			HttpStatusCode statusCode, WebRequest request) {
		ErrorCode errorCode = mapToErrorCode(e);
		log.error("Spring error occurred: {} - {}", errorCode.getMessage(), e.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode));
	}

	/**
	 * 사용자가 정의한 ApiException 예외 처리
	 */
	@ExceptionHandler(ApiException.class)
	public ResponseEntity<ApiResponse<Void>> handleApiException(ApiException e) {
		ErrorCode errorCode = e.getErrorCode();
		log.error("API error occurred: {}", errorCode.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode));
	}

	/**
	 * 발생한 예외를 ErrorCode enum 값으로 매핑하는 메서드 예외 종류에 따라 적절한 ErrorCode를 반환
	 */
	@ExceptionHandler(Exception.class)
	public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception e) {
		ErrorCode errorCode = ErrorCode.SERVER_ERROR;
		log.error("General error occurred: {} - {}", errorCode.getMessage(), e.getMessage());
		return ResponseEntity.status(errorCode.getStatus()).body(ApiResponse.fail(errorCode));
	}

	/**
     * 발생한 예외를 ErrorCode enum 값으로 매핑하는 메서드
     * 예외 종류에 따라 적절한 ErrorCode를 반환
     */
	private ErrorCode mapToErrorCode(Exception e) {
	    if (e instanceof MethodArgumentNotValidException
	            || e instanceof HttpMessageNotReadableException
	            || e instanceof HandlerMethodValidationException
	            || e instanceof MissingServletRequestParameterException
	            || e instanceof HttpRequestMethodNotSupportedException
	            || e instanceof HttpMediaTypeNotSupportedException
	            || e instanceof TypeMismatchException
	            || e instanceof ServletRequestBindingException
	            || e instanceof MissingPathVariableException
	            || e instanceof HttpMediaTypeNotAcceptableException) {
	        return ErrorCode.BAD_REQUEST;
	    }

	    if (e instanceof NoHandlerFoundException
	            || e instanceof NoResourceFoundException) {
	        return ErrorCode.NOT_FOUND;
	    }

	    return ErrorCode.SERVER_ERROR;
	}

}