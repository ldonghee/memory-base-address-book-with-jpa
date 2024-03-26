package com.hyundai.test.address.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.hyundai.test.address.config.QueryDslConfig;
import com.hyundai.test.address.domain.AddressBookEntity;
import com.hyundai.test.address.dto.AddressBookSearchCondition;


@Import({QueryDslConfig.class})
@DataJpaTest
@ActiveProfiles("test")
public class QueryDslAddressBookRepositoryTest {

	@Autowired
	private AddressBookJpaRepository addressBookJpaRepository;


	@BeforeEach
	void setup() {
		List<AddressBookEntity> addressBookEntityList = new ArrayList<>();
		for (int i = 1; i <= 5; i++) {
			addressBookEntityList.add(new AddressBookEntity(Long.valueOf(i + ""), "010-1111-111" + i, "test" + i + "@test.com", "address" + i, "name" + i));
		}
		addressBookJpaRepository.saveAll(addressBookEntityList);
	}

	@Test
	@DisplayName("유효하지 않은 검색 조건 주소록 조회")
	void invalid_get_address_book() {
		// given & when & then
		Assertions.assertThrows(IllegalArgumentException.class, () -> addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("phone_number2", "010-1111-1113")));
		Assertions.assertThrows(IllegalArgumentException.class, () -> addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("ema", "010-1111-1113")));
		Assertions.assertThrows(IllegalArgumentException.class, () -> addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("phone_number", "010-1111-1113", "phone", "asc")));
		Assertions.assertThrows(IllegalArgumentException.class, () -> addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("phone_number", "010-1111-1113", "na", "asc")));
	}

	@Test
	@DisplayName("폰넘버 검색 주소록 조회")
	void get_address_book_equal_phone_number() {
		// given & when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("phone_number", "010-1111-1113"));
		// then
		assertAll(
				() -> assertThat(result.size()).isEqualTo(1),
				() -> assertThat(result.get(0).getPhoneNumber()).startsWith("010-1111-1113"),
				() -> assertThat(result.get(0).getEmail()).startsWith("test3@test.com"),
				() -> assertThat(result.get(0).getAddress()).startsWith("address3"),
				() -> assertThat(result.get(0).getName()).startsWith("name3"));
	}

	@Test
	@DisplayName("이메일 검색 주소록 조회")
	void get_address_book_equal_email() {
		// given & when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("email", "test3@test.com"));
		// then
		assertAll(
				() -> assertThat(result.size()).isEqualTo(1),
				() -> assertThat(result.get(0).getPhoneNumber()).startsWith("010-1111-1113"),
				() -> assertThat(result.get(0).getEmail()).startsWith("test3@test.com"),
				() -> assertThat(result.get(0).getAddress()).startsWith("address3"),
				() -> assertThat(result.get(0).getName()).startsWith("name3"));
	}

	@Test
	@DisplayName("이름 라이크 검색 주소록 조회")
	void get_address_book_like_name() {
		// given & when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("name", "name1"));
		// then
		assertThat(result.size()).isEqualTo(1);
	}

	@Test
	@DisplayName("이름 라이크 검색 및 이름 기준 내림차순 주소록 조회")
	void get_address_book_like_name_and_order_name_desc() {
		// given
		List<AddressBookEntity> addressBookEntityList = new ArrayList<>();
		addressBookEntityList.add(new AddressBookEntity(10L, "010-1234-1234", "dhlee1@test.com", "주소1", "테스트1"));
		addressBookEntityList.add(new AddressBookEntity(11L, "010-1234-1235", "dhlee2@test.com", "주소2", "테스트2"));
		addressBookEntityList.add(new AddressBookEntity(12L, "010-1234-1236", "dhlee3@test.com", "주소3", "테스트3"));
		addressBookJpaRepository.saveAll(addressBookEntityList);

		// when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("name", "테스트", "name", "desc"));

		// then
		assertAll(
				() -> assertThat(result.size()).isEqualTo(3),
				() -> assertThat(result.get(0).getName()).isEqualTo("테스트3"),
				() -> assertThat(result.get(1).getName()).isEqualTo("테스트2"),
				() -> assertThat(result.get(2).getName()).isEqualTo("테스트1"));
	}

	@Test
	@DisplayName("주소 라이크 검색 주소록 조회")
	void get_address_book_like_address() {
		// given & when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("address", "address"));
		// then
		assertThat(result.size()).isEqualTo(5);
	}

	@Test
	@DisplayName("주소 라이크 검색 및 주소 오름차순 주소록 조회")
	void get_address_book_like_address_and_order_address_asc() {
		// given
		List<AddressBookEntity> addressBookEntityList = new ArrayList<>();
		addressBookEntityList.add(new AddressBookEntity(10L, "010-1234-1234", "dhlee1@test.com", "주소1", "테스트1"));
		addressBookEntityList.add(new AddressBookEntity(11L, "010-1234-1235", "dhlee2@test.com", "주소2", "테스트2"));
		addressBookEntityList.add(new AddressBookEntity(12L, "010-1234-1236", "dhlee3@test.com", "주소3", "테스트3"));
		addressBookJpaRepository.saveAll(addressBookEntityList);

		// when
		List<AddressBookEntity> result = addressBookJpaRepository.findAllAddressBook(new AddressBookSearchCondition("address", "주소", "address", "asc"));

		// then
		assertAll(
				() -> assertThat(result.size()).isEqualTo(3),
				() -> assertThat(result.get(0).getAddress()).isEqualTo("주소1"),
				() -> assertThat(result.get(1).getAddress()).isEqualTo("주소2"),
				() -> assertThat(result.get(2).getAddress()).isEqualTo("주소3"));
	}
}
