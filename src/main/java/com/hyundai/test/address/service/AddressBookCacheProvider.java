package com.hyundai.test.address.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.hyundai.test.address.dto.AddressBookDto;

@Component
public class AddressBookCacheProvider {
	private final Map<String, String> addressBookCacheMap = new LinkedHashMap<>();

	public void remove(String key) {
		addressBookCacheMap.remove(key);
	}

	public void removeAll(List<AddressBookDto> deleteAddressBookDtoList) {
		for (AddressBookDto deleteAddressBook : deleteAddressBookDtoList) {
			remove(deleteAddressBook.getPhoneNumber());
		}
	}

	public void addAll(List<AddressBookDto> addressBookDtoList) {
		for (AddressBookDto addressBook : addressBookDtoList) {
			add(addressBook);
		}
	}

	public void add(AddressBookDto addressBookDto) {
		addressBookCacheMap.put(addressBookDto.getPhoneNumber(), addressBookDto.toCsvFileLine());
	}

	public List<String> getValues() {
		List<String> values = new ArrayList<>();
		values.add(0, "\"주소\",\"연락처\",\"이메일\",\"이름\"");
		for (String key : addressBookCacheMap.keySet()) {
			values.add(addressBookCacheMap.get(key));
		}
		return values;
	}

	public void update(AddressBookDto updateAddressDto) {
		add(updateAddressDto);
	}
}
