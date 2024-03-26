package com.hyundai.test.address.dto;

import java.util.Objects;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.hyundai.test.address.domain.AddressBookEntity;

@Getter
@AllArgsConstructor
public class AddressBookUpdateRequest {
	private String phoneNumber;
	private String email;
	private String address;
	private String name;

	public AddressBookEntity toEntity() {
		return new AddressBookEntity(phoneNumber, email, address, name);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		AddressBookUpdateRequest that = (AddressBookUpdateRequest) o;
		return Objects.equals(phoneNumber, that.phoneNumber) && Objects.equals(email, that.email) && Objects.equals(address,
																													that.address)
			   && Objects.equals(name, that.name);
	}

	@Override
	public int hashCode() {
		return Objects.hash(phoneNumber, email, address, name);
	}
}
