package com.hyundai.test.address.common.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.extern.slf4j.Slf4j;

import com.hyundai.test.address.common.dto.ExceptionResponse;

@Slf4j
@RestController
@ControllerAdvice
public class ExceptionAdvice extends ResponseEntityExceptionHandler {
	/**
	 * Exception 발생 시, 실행되는 메서드
	 */
	@ExceptionHandler(Exception.class)
	public final ResponseEntity<ExceptionResponse> handleAllExceptions(Exception e) {
		log.error("Not Handle Exception", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
	}

	/**
	 * Runtime Exception 발생 시, 실행되는 메서드
	 */
	@ExceptionHandler(RuntimeException.class)
	protected ResponseEntity<ExceptionResponse> handleBusinessException(RuntimeException e) {
		log.error("Not Handle Runtime Exception", e);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getMessage()));
	}
}
