package com.hyundai.test.address.repository;

import java.util.List;

import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.domain.AddressBookEntity;

public interface QueryDslAddressBookRepository {
	List<AddressBookEntity> findAllAddressBook(AddressBookSearchCondition addressBookSearchCondition);
}
