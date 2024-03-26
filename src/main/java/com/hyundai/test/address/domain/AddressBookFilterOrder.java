package com.hyundai.test.address.domain;

import java.util.Arrays;
import java.util.Locale;

import lombok.Getter;

@Getter
public enum AddressBookFilterOrder {
	ASC("ASC"),
	DESC("DESC");

	private final String order;

	AddressBookFilterOrder(String order) {
		this.order = order;
	}

	public static AddressBookFilterOrder of(String order) {
		return Arrays.stream(AddressBookFilterOrder.values())
					 .filter(resultType -> resultType.order.equals(order.toUpperCase(Locale.ROOT)))
					 .findFirst()
					 .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 정렬 순서입니다."));
	}
}
