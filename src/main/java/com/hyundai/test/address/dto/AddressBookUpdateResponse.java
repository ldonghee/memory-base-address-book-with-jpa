package com.hyundai.test.address.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AddressBookUpdateResponse {
	private final AddressBookDto beforeAddressBook;
	private final AddressBookDto updateAddressBook;
}
