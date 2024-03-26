package com.hyundai.test.address.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import com.hyundai.test.address.domain.AddressBookEntity;

@Getter
@AllArgsConstructor
public class AddressBookDto {
	private String phoneNumber;
	private String email;
	private String address;
	private String name;

	public AddressBookDto(AddressBookEntity addressBook) {
		this.phoneNumber = addressBook.getPhoneNumber();
		this.email = addressBook.getEmail();
		this.address = addressBook.getAddress();
		this.name = addressBook.getName();
	}

	public AddressBookEntity toEntity() {
		return new AddressBookEntity(phoneNumber, email, address, name);
	}

	@Override
	public String toString() {
		return "{" +
			   "phoneNumber='" + phoneNumber + '\'' +
			   ", email='" + email + '\'' +
			   ", address='" + address + '\'' +
			   ", name='" + name + '\'' +
			   '}';
	}

	public String toCsvFileLine() {
		return address + "," + phoneNumber + "," + email + "," +  name;
	}
}
