package com.hyundai.test.address.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;

import com.hyundai.test.address.domain.AddressBookEntity;
import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.dto.AddressBookUpdateRequest;
import com.hyundai.test.address.dto.AddressBookUpdateResponse;
import com.hyundai.test.address.repository.AddressBookJpaRepository;

@Service
@Transactional
@RequiredArgsConstructor
public class AddressBookService {
	private final AddressBookJpaRepository addressBookJpaRepository;
	private final AddressBookCacheProvider addressBookCacheProvider;

	@Transactional(readOnly = true)
	public List<AddressBookDto> getAddressBooks(AddressBookSearchCondition condition) {
		List<AddressBookEntity> addressBooks = addressBookJpaRepository.findAllAddressBook(condition);
		return addressBooks.stream()
						   .map(AddressBookDto::new)
						   .collect(Collectors.toList());
	}

	public AddressBookDto deleteAddressBook(String phoneNumber) {
		Optional<AddressBookEntity> findAddressBook = addressBookJpaRepository.findByPhoneNumber(phoneNumber);
		if (findAddressBook.isEmpty()) {
			throw new NoSuchElementException("데이터가 존재하지 않습니다");
		}
		addressBookJpaRepository.deleteByPhoneNumber(phoneNumber);
		addressBookCacheProvider.remove(phoneNumber);
		return new AddressBookDto(findAddressBook.get());
	}


	public List<AddressBookDto> deleteAddressBookByName(String name) {
		List<AddressBookEntity> deleteAddressBooks = addressBookJpaRepository.findByNameContaining(name);
		if (deleteAddressBooks.isEmpty()) {
			throw new NoSuchElementException("데이터가 존재하지 않습니다");
		}
		addressBookJpaRepository.deleteAll(deleteAddressBooks);
		List<AddressBookDto> deleteAddressBookDtoList = deleteAddressBooks.stream()
																		  .map(AddressBookDto::new)
																		  .toList();
		addressBookCacheProvider.removeAll(deleteAddressBookDtoList);
		return deleteAddressBookDtoList;
	}

	public AddressBookUpdateResponse updateAddressBook(String phoneNumber, AddressBookUpdateRequest request) {
		Optional<AddressBookEntity> findAddressBookEntity = addressBookJpaRepository.findByPhoneNumber(phoneNumber);
		if (findAddressBookEntity.isEmpty()) {
			throw new NoSuchElementException("데이터가 존재하지 않습니다");
		}
		AddressBookDto beforeAddressBookDto = new AddressBookDto(findAddressBookEntity.get());
		AddressBookEntity updateAddressBookEntity = findAddressBookEntity.get();
		updateAddressBookEntity.update(request.toEntity());
		AddressBookDto updateAddressDto = new AddressBookDto(updateAddressBookEntity);
		addressBookCacheProvider.update(updateAddressDto);
		return new AddressBookUpdateResponse(beforeAddressBookDto, updateAddressDto);
	}
}
