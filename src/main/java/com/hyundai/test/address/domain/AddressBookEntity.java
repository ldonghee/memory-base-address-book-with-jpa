package com.hyundai.test.address.domain;


import lombok.Getter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Getter
@Entity
@Table(name = "address_book")
public class AddressBookEntity extends BaseEntity {
	private static final String EMAIL_PATTERN = "^[A-Za-z0-9+_.-]+(?<!\\.)@(?=.{1,255}$)(?!\\.)[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
	private static final String PHONE_NUMBER_PATTERN = "^010(\\d{7,8}|-(\\d{3}-\\d{4}|\\d{4}-\\d{4}))$";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "phone_number", unique = true, length = 50)
	private String phoneNumber;

	@Column(name = "email", unique = true, length = 100)
	private String email;

	@Column(name = "address", length = 500)
	private String address;

	@Column(name = "name", length = 100)
	private String name;

	protected AddressBookEntity() {
	}

	public AddressBookEntity(String phoneNumber, String email, String address, String name) {
		validate(phoneNumber, email, address, name);

		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.name = name;
	}

	public AddressBookEntity(Long id, String phoneNumber, String email, String address, String name) {
		validate(phoneNumber, email, address, name);

		this.id = id;
		this.phoneNumber = phoneNumber;
		this.email = email;
		this.address = address;
		this.name = name;
	}

	private void validate(String phoneNumber, String email, String address, String name) {
		if (!phoneNumber.matches(PHONE_NUMBER_PATTERN) || phoneNumber.length() > 50) {
			throw new IllegalArgumentException("유효하지 않은 전화번호 데이터 형식입니다.");
		}
		if (!email.matches(EMAIL_PATTERN) || email.length() > 100) {
			throw new IllegalArgumentException("유효하지 않은 이메일 데이터 형식입니다.");
		}
		if (address.length() > 500) {
			throw new IllegalArgumentException("주소 데이터 값이 너무 깁니다.");
		}
		if (name.length() > 100) {
			throw new IllegalArgumentException("이름 데이터 값이 너무 깁니다.");
		}
	}

	public void update(AddressBookEntity updateAddressBook) {
		this.email = updateAddressBook.getEmail();
		this.address = updateAddressBook.getAddress();
		this.name = updateAddressBook.getName();
	}
}
