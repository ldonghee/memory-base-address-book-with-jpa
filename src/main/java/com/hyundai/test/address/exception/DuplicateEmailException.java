package com.hyundai.test.address.exception;

public class DuplicateEmailException extends RuntimeException {
	public DuplicateEmailException() {
		super("중복된 이메일 입니다.");
	}
}
