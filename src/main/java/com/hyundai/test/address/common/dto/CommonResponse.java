package com.hyundai.test.address.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CommonResponse<T> {
	private int statusCode;
	private String statusMessage;
	private T result;
}
