package com.hyundai.test.address.domain;

import java.util.Arrays;
import java.util.Locale;

import lombok.Getter;

@Getter
public enum AddressBookFilterType {
	NAME("name"),
	PHONE_NUMBER("phone_number"),
	ADDRESS("address"),
	EMAIL("email");

	private final String type;

	AddressBookFilterType(String type) {
		this.type = type;
	}

	public static AddressBookFilterType of(String type) {
		return Arrays.stream(AddressBookFilterType.values())
					 .filter(resultType -> resultType.type.equals(type.toLowerCase(Locale.ROOT)))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 검색 타입입니다."));
	}
}
