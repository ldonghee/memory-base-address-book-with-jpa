package com.hyundai.test.address.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.hyundai.test.address.common.dto.CommonResponse;
import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.dto.AddressBookUpdateRequest;
import com.hyundai.test.address.dto.AddressBookUpdateResponse;
import com.hyundai.test.address.service.AddressBookService;

/**
 * 1. 이름을 라이크 검색해서 해당 이름의 주소록을 모두 삭제(삭제)
 * 2. 이름을 라이크 검색해서 해당 주소록 리스트를 조회한다(조회)
 * 3. 폰넘버를 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 4. 주소를 라이크 검색해서 해당 주소록 리스트를 조회한다.(조회)
 * 5. 폰넘버를 받아 해당 내용의 데이터를 주소록에서 모두 삭제한다.(삭제)
 * 6. 폰넘버를 받아 전달받은 주소록 데이터를 주소록에 변경 반영한다.(업데이트)
 *
 * 주의) 삭제의 경우에는 삭제 후 삭제 된 주소로 데이터를 리턴해준다.
 * 주의) 업데이트의 경우에는 업데이트 전 데이터와 업데이트 후 데이터를 구분해서 리턴해준다.
 */
@RestController
@RequiredArgsConstructor
public class AddressBookController {
	private final AddressBookService addressBookService;
//	private final AddressBookFileIOService addressBookFileIOService;

	@GetMapping("/address-books")
	public ResponseEntity getAddressBooks(@RequestParam(required = false) String filterValue,
										  @RequestParam(required = false) String filterType,
										  @RequestParam(required = false, defaultValue = "phone_number") String sortBy,
										  @RequestParam(required = false, defaultValue = "ASC") String sortOrder) {
		List<AddressBookDto> addressBooks = addressBookService.getAddressBooks(new AddressBookSearchCondition(filterType, filterValue, sortBy, sortOrder));
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), addressBooks));
	}

	@DeleteMapping("/address-books/{phone_number}")
	public ResponseEntity deleteAddressBook(@PathVariable(name = "phone_number") String phoneNumber) {
		AddressBookDto deleteAddressBook = addressBookService.deleteAddressBook(phoneNumber);
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), deleteAddressBook));
	}

	@DeleteMapping("/address-books")
	public ResponseEntity deleteAddressBookByName(@RequestParam String name) {
		List<AddressBookDto> deleteAddressBooks = addressBookService.deleteAddressBookByName(name);
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), deleteAddressBooks));
	}

	@PutMapping("/address-books/{phone_number}")
	public ResponseEntity updateAddressBook(@PathVariable(name = "phone_number") String phoneNumber, @RequestBody AddressBookUpdateRequest request) {
		AddressBookUpdateResponse addressBookUpdateResponse = addressBookService.updateAddressBook(phoneNumber, request);
		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), addressBookUpdateResponse));
	}

//	@PutMapping("/address-books")
//	public ResponseEntity test() {
//		addressBookFileIOService.export("src/main/resources/data/output/success_address2.csv");
//		return ResponseEntity.ok(new CommonResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), ""));
//	}
}
