package com.hyundai.test.address.exception;

public class DuplicatePhoneNumberException extends RuntimeException {
	public DuplicatePhoneNumberException() {
		super("중복된 전화번호 입니다.");
	}
}
