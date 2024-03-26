package com.hyundai.test.address.domain;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.exception.DuplicateEmailException;
import com.hyundai.test.address.exception.DuplicatePhoneNumberException;

@Slf4j
@Getter
public class AddressBookEntities {
	private final Set<String> emailHashSet = new HashSet<>();
	private final Set<String> phoneNumberHashSet = new HashSet<>();
	private final List<AddressBookEntity> addressBookEntityList = new ArrayList<>();

	public AddressBookEntities(List<AddressBookDto> addressBookDtoList) {
		for (AddressBookDto dto : addressBookDtoList) {
			add(dto);
		}
	}

	public void add(AddressBookDto dto) {
		try {
			AddressBookEntity entity = dto.toEntity();
			checkDuplicateEmail(entity.getEmail());
			checkDuplicatePhoneNumber(entity.getPhoneNumber());
			addressBookEntityList.add(entity);
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			log.warn("데이터 형식 에러 : " + dto);
		} catch (DuplicateEmailException | DuplicatePhoneNumberException e) {
			log.warn("중복된 데이터 에러 : " + dto);
		}
	}

	private void checkDuplicateEmail(String email) {
		if (emailHashSet.contains(email)) {
			throw new DuplicateEmailException();
		}
		emailHashSet.add(email);
	}

	private void checkDuplicatePhoneNumber(String phoneNumber) {
		if (phoneNumberHashSet.contains(phoneNumber)) {
			throw new DuplicatePhoneNumberException();
		}
		phoneNumberHashSet.add(phoneNumber);
	}

}
