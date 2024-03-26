package com.hyundai.test.address.domain;

import static org.junit.jupiter.api.Assertions.assertAll;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AddressEntityTest {
	@Test
	@DisplayName("생성 테스트")
	void create() {
		// given & when & then
		assertAll(
				() -> new AddressBookEntity("010-1111-1111", "test@test.com", "주소", "테스트"),
				() -> new AddressBookEntity("010-111-1111", "test@test.com", "주소", "테스트"),
				() -> new AddressBookEntity("01011111111", "test@test.com", "주소", "테스트"));
	}

	@Test
	@DisplayName("유효하지 않은 전화번호 생성 예외 테스트")
	void invalid_phone_number_create_exception() {
		// given & when & then
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-12345-678", "test@test.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-1234-6-78", "test@test.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("01-1234-678", "test@test.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("0101234-6789", "test@test.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("0101234678-9", "test@test.com", "주소", "테스트"));
	}

	@Test
	@DisplayName("유효하지 않은 이메일 생성 예외 테스트")
	void invalid_email_create_exception() {
		// given & when & then
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-1111-1111", "username@.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-1111-1111", "username@domain", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-1111-1111", "username#domain.com", "주소", "테스트"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> new AddressBookEntity("010-1111-1111", "username@@domain.com", "주소", "테스트"));
	}
}
