package com.hyundai.test.address.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.hyundai.test.address.domain.AddressBookEntity;
import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.dto.AddressBookUpdateRequest;
import com.hyundai.test.address.dto.AddressBookUpdateResponse;
import com.hyundai.test.address.repository.AddressBookJpaRepository;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class AddressBookServiceTest {
	@Mock
	private AddressBookJpaRepository addressBookJpaRepository;
	@Mock
	private AddressBookCacheProvider addressBookCacheProvider;

	private AddressBookService addressBookService;

	@BeforeEach
	public void setUp() {
		addressBookService = new AddressBookService(addressBookJpaRepository, addressBookCacheProvider);
	}

	@Test
	@DisplayName("필터/정렬 파라미터에 따른 주소록 조회")
	void get_address_books() {
		// given
		AddressBookSearchCondition condition = new AddressBookSearchCondition("name", "이몽", "phone_number", "asc");
		List<AddressBookEntity> addressBookEntityList = Arrays.asList(new AddressBookEntity(1L, "010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡"),
																	  new AddressBookEntity(2L, "010-000-0004", "lee3@hyundai.com", "전라북도 남원시",
																							"이몽룡"));
		given(addressBookJpaRepository.findAllAddressBook(condition)).willReturn(addressBookEntityList);
		// when
		List<AddressBookDto> addressBookDtoList = addressBookService.getAddressBooks(condition);
		// then
		assertAll(
				() -> assertThat(addressBookDtoList.size()).isEqualTo(2),
				() -> assertThat(addressBookDtoList.get(0).getPhoneNumber()).isEqualTo(addressBookDtoList.get(0).getPhoneNumber()),
				() -> assertThat(addressBookDtoList.get(0).getEmail()).isEqualTo(addressBookDtoList.get(0).getEmail()),
				() -> assertThat(addressBookDtoList.get(0).getAddress()).isEqualTo(addressBookDtoList.get(0).getAddress()),
				() -> assertThat(addressBookDtoList.get(0).getName()).isEqualTo(addressBookDtoList.get(0).getName()),
				() -> assertThat(addressBookDtoList.get(1).getPhoneNumber()).isEqualTo(addressBookDtoList.get(1).getPhoneNumber()),
				() -> assertThat(addressBookDtoList.get(1).getEmail()).isEqualTo(addressBookDtoList.get(1).getEmail()),
				() -> assertThat(addressBookDtoList.get(1).getAddress()).isEqualTo(addressBookDtoList.get(1).getAddress()),
				() -> assertThat(addressBookDtoList.get(1).getName()).isEqualTo(addressBookDtoList.get(1).getName()));
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 삭제")
	void delete_address_book() {
		// given
		AddressBookEntity addressBookEntity = new AddressBookEntity(1L, "010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡");
		given(addressBookJpaRepository.findByPhoneNumber("010-000-0001")).willReturn(Optional.of(addressBookEntity));
		// when
		AddressBookDto addressBookDto = addressBookService.deleteAddressBook("010-000-0001");
		// then
		assertAll(
				() -> assertThat(addressBookDto.getPhoneNumber()).isEqualTo(addressBookEntity.getPhoneNumber()),
				() -> assertThat(addressBookDto.getEmail()).isEqualTo(addressBookEntity.getEmail()),
				() -> assertThat(addressBookDto.getAddress()).isEqualTo(addressBookEntity.getAddress()),
				() -> assertThat(addressBookDto.getName()).isEqualTo(addressBookEntity.getName()));
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 삭제 시, 이미 삭제한 대상 예외 발생")
	void exception_delete_address_book() {
		// given
		given(addressBookJpaRepository.findByPhoneNumber("010-000-0001")).willReturn(Optional.empty());
		// when & then
		Assertions.assertThrows(NoSuchElementException.class, () -> addressBookService.deleteAddressBook("010-000-0001"));
	}


	@Test
	@DisplayName("이름 파라미터 값을 포함하는 주소록 삭제")
	void delete_address_book_by_name() {
		// given
		List<AddressBookEntity> addressBookEntityList = Arrays.asList(new AddressBookEntity(1L, "010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡"),
																	  new AddressBookEntity(2L, "010-000-0004", "lee3@hyundai.com", "전라북도 남원시",
																							"이몽룡"));
		given(addressBookJpaRepository.findByNameContaining("이몽")).willReturn(addressBookEntityList);
		// when
		List<AddressBookDto> addressBookDtoList = addressBookService.deleteAddressBookByName("이몽");
		// then
		assertAll(
				() -> assertThat(addressBookDtoList.size()).isEqualTo(2),
				() -> assertThat(addressBookDtoList.get(0).getPhoneNumber()).isEqualTo(addressBookDtoList.get(0).getPhoneNumber()),
				() -> assertThat(addressBookDtoList.get(0).getEmail()).isEqualTo(addressBookDtoList.get(0).getEmail()),
				() -> assertThat(addressBookDtoList.get(0).getAddress()).isEqualTo(addressBookDtoList.get(0).getAddress()),
				() -> assertThat(addressBookDtoList.get(0).getName()).isEqualTo(addressBookDtoList.get(0).getName()),
				() -> assertThat(addressBookDtoList.get(1).getPhoneNumber()).isEqualTo(addressBookDtoList.get(1).getPhoneNumber()),
				() -> assertThat(addressBookDtoList.get(1).getEmail()).isEqualTo(addressBookDtoList.get(1).getEmail()),
				() -> assertThat(addressBookDtoList.get(1).getAddress()).isEqualTo(addressBookDtoList.get(1).getAddress()),
				() -> assertThat(addressBookDtoList.get(1).getName()).isEqualTo(addressBookDtoList.get(1).getName()));
	}

	@Test
	@DisplayName("이름 파라미터 값을 포함하는 주소록 삭제 시, 존재하지 않는 대상 예외 발생")
	void exception_delete_address_book_by_name() {
		// given
		given(addressBookJpaRepository.findByNameContaining("이몽")).willReturn(new ArrayList<>());
		// when & then
		Assertions.assertThrows(NoSuchElementException.class, () -> addressBookService.deleteAddressBookByName("이몽"));
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 수정")
	void update_address_book() {
		// given
		AddressBookDto addressBookDto = new AddressBookDto("010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡");
		AddressBookUpdateRequest updateRequest = new AddressBookUpdateRequest("010-000-0001", "test@hyundai.com", "서울시 금천구", "테스트");
		given(addressBookJpaRepository.findByPhoneNumber("010-000-0001")).willReturn(Optional.of(addressBookDto.toEntity()));
		// when
		AddressBookUpdateResponse addressBookUpdateResponse = addressBookService.updateAddressBook("010-000-0001", updateRequest);
		AddressBookDto updateAddressBook = addressBookUpdateResponse.getUpdateAddressBook();
		AddressBookDto beforeAddressBook = addressBookUpdateResponse.getBeforeAddressBook();
		// then
		assertAll(
				() -> assertThat(updateAddressBook.getPhoneNumber()).isEqualTo(updateRequest.getPhoneNumber()),
				() -> assertThat(updateAddressBook.getEmail()).isEqualTo(updateRequest.getEmail()),
				() -> assertThat(updateAddressBook.getAddress()).isEqualTo(updateRequest.getAddress()),
				() -> assertThat(updateAddressBook.getName()).isEqualTo(updateRequest.getName()),

				() -> assertThat(beforeAddressBook.getPhoneNumber()).isEqualTo(addressBookDto.getPhoneNumber()),
				() -> assertThat(beforeAddressBook.getEmail()).isEqualTo(addressBookDto.getEmail()),
				() -> assertThat(beforeAddressBook.getAddress()).isEqualTo(addressBookDto.getAddress()),
				() -> assertThat(beforeAddressBook.getName()).isEqualTo(addressBookDto.getName()));
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 수정 시, 존재하지 않는 대상 예외 발생")
	void exception_update_address_book() {
		// given
		AddressBookUpdateRequest updateRequest = new AddressBookUpdateRequest("010-000-0001", "test@hyundai.com", "서울시 금천구", "테스트");
		given(addressBookJpaRepository.findByPhoneNumber("010-000-0001")).willReturn(Optional.empty());
		// when & then
		Assertions.assertThrows(NoSuchElementException.class, () -> addressBookService.updateAddressBook("010-000-0001", updateRequest));
	}

}
