package com.hyundai.test.address.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ExceptionResponse {
	private int statusCode;
	private String errorMessage;
}
