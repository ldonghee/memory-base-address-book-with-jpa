package com.hyundai.test.address.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.dto.AddressBookSearchCondition;
import com.hyundai.test.address.dto.AddressBookUpdateRequest;
import com.hyundai.test.address.dto.AddressBookUpdateResponse;
import com.hyundai.test.address.service.AddressBookService;

@ActiveProfiles("test")
@WebMvcTest(AddressBookController.class)
@MockBean(JpaMetamodelMappingContext.class)
class AddressBookControllerTest {
	@Autowired
	private MockMvc mockMvc;
	@MockBean
	private AddressBookService addressBookService;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("필터/정렬 파라미터에 따른 주소록 조회")
	void get_address_books() throws Exception {
		// given
		AddressBookSearchCondition condition = new AddressBookSearchCondition("name", "이몽", "phone_number", "asc");
		List<AddressBookDto> addressBookDtoList = Arrays.asList(new AddressBookDto("010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡"),
																new AddressBookDto("010-000-0004", "lee3@hyundai.com", "전라북도 남원시", "이몽룡"));
		given(addressBookService.getAddressBooks(condition)).willReturn(addressBookDtoList);
		// when
		ResultActions result = mockMvc.perform(get("/address-books")
													   .param("filterValue", "이몽")
													   .param("filterType", "name")
													   .param("sortBy", "phone_number")
													   .param("sortOrder", "asc"))
									  .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result[0].phoneNumber").value(addressBookDtoList.get(0).getPhoneNumber()))
			  .andExpect(jsonPath("$.result[0].email").value(addressBookDtoList.get(0).getEmail()))
			  .andExpect(jsonPath("$.result[0].address").value(addressBookDtoList.get(0).getAddress()))
			  .andExpect(jsonPath("$.result[0].name").value(addressBookDtoList.get(0).getName()))
			  .andExpect(jsonPath("$.result[1].phoneNumber").value(addressBookDtoList.get(1).getPhoneNumber()))
			  .andExpect(jsonPath("$.result[1].email").value(addressBookDtoList.get(1).getEmail()))
			  .andExpect(jsonPath("$.result[1].address").value(addressBookDtoList.get(1).getAddress()))
			  .andExpect(jsonPath("$.result[1].name").value(addressBookDtoList.get(1).getName()))
			  .andDo(print());
	}

	@Test
	@DisplayName("잘못된 필터/정렬 파라미터 따른 주소록 조회 시, 예외 발생")
	void exception_get_address_books() throws Exception {
		// given & when
		ResultActions result = mockMvc.perform(get("/address-books")
													   .param("filterValue", "이몽")
													   .param("filterType", "test")
													   .param("sortBy", "phone_number")
													   .param("sortOrder", "asc"))
									  .andDo(print());
		// then
		result.andExpect(status().isInternalServerError())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			  .andExpect(jsonPath("$.errorMessage").value("유효하지 않은 검색 타입입니다."))
			  .andDo(print());
	}

	@Test
	@DisplayName("잘못된 정렬 순서 파라미터 따른 주소록 조회 시, 예외 발생")
	void exception_get_address_books2() throws Exception {
		// given & when
		ResultActions result = mockMvc.perform(get("/address-books")
													   .param("filterValue", "이몽")
													   .param("filterType", "name")
													   .param("sortBy", "name")
													   .param("sortOrder", "test"))
									  .andDo(print());
		// then
		result.andExpect(status().isInternalServerError())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.INTERNAL_SERVER_ERROR.value()))
			  .andExpect(jsonPath("$.errorMessage").value("유효하지 않은 정렬 순서입니다."))
			  .andDo(print());
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 삭제")
	void delete_address_book() throws Exception {
		// given
		AddressBookDto deleteAddressBookDto = new AddressBookDto("010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡");
		given(addressBookService.deleteAddressBook(deleteAddressBookDto.getPhoneNumber())).willReturn(deleteAddressBookDto);
		// when
		ResultActions result = mockMvc.perform(delete("/address-books/{phone_number}", "010-000-0001"))
									  .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result.phoneNumber").value(deleteAddressBookDto.getPhoneNumber()))
			  .andExpect(jsonPath("$.result.email").value(deleteAddressBookDto.getEmail()))
			  .andExpect(jsonPath("$.result.address").value(deleteAddressBookDto.getAddress()))
			  .andExpect(jsonPath("$.result.name").value(deleteAddressBookDto.getName()))
			  .andDo(print());
	}

	@Test
	@DisplayName("이름 파라미터 값을 포함하는 주소록 삭제")
	void delete_address_book_by_name() throws Exception {
		// given
		List<AddressBookDto> addressBookDtoList = Arrays.asList(new AddressBookDto("010-000-0001", "lee@hyundai.com", "경기도 성남시", "이몽룡"),
																new AddressBookDto("010-000-0004", "lee3@hyundai.com", "전라북도 남원시", "이몽룡"));
		given(addressBookService.deleteAddressBookByName("이몽")).willReturn(addressBookDtoList);
		// when
		ResultActions result = mockMvc.perform(delete("/address-books").param("name", "이몽"))
									  .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result[0].phoneNumber").value(addressBookDtoList.get(0).getPhoneNumber()))
			  .andExpect(jsonPath("$.result[0].email").value(addressBookDtoList.get(0).getEmail()))
			  .andExpect(jsonPath("$.result[0].address").value(addressBookDtoList.get(0).getAddress()))
			  .andExpect(jsonPath("$.result[0].name").value(addressBookDtoList.get(0).getName()))
			  .andExpect(jsonPath("$.result[1].phoneNumber").value(addressBookDtoList.get(1).getPhoneNumber()))
			  .andExpect(jsonPath("$.result[1].email").value(addressBookDtoList.get(1).getEmail()))
			  .andExpect(jsonPath("$.result[1].address").value(addressBookDtoList.get(1).getAddress()))
			  .andExpect(jsonPath("$.result[1].name").value(addressBookDtoList.get(1).getName()))
			  .andDo(print());
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 수정")
	void update_address_book() throws Exception {
		// given
		AddressBookDto addressBookDto = new AddressBookDto("010-000-0004", "lee3@hyundai.com", "전라북도 남원시", "이몽룡");
		AddressBookUpdateRequest updateRequest = new AddressBookUpdateRequest("010-000-0004", "test@hyundai.com", "서울시 금천구", "테스트");
		AddressBookDto updateAddressDto =
				new AddressBookDto(updateRequest.getPhoneNumber(), updateRequest.getEmail(), updateRequest.getAddress(), updateRequest.getName());
		AddressBookUpdateResponse addressBookUpdateResponse = new AddressBookUpdateResponse(addressBookDto, updateAddressDto);
		given(addressBookService.updateAddressBook(updateRequest.getPhoneNumber(), updateRequest)).willReturn(addressBookUpdateResponse);
		// when
		ResultActions result =
				mockMvc.perform(put("/address-books/{phone_number}", updateRequest.getPhoneNumber())
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(updateRequest)))
					   .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result.beforeAddressBook.phoneNumber").value(addressBookDto.getPhoneNumber()))
			  .andExpect(jsonPath("$.result.beforeAddressBook.email").value(addressBookDto.getEmail()))
			  .andExpect(jsonPath("$.result.beforeAddressBook.address").value(addressBookDto.getAddress()))
			  .andExpect(jsonPath("$.result.beforeAddressBook.name").value(addressBookDto.getName()))
			  .andExpect(jsonPath("$.result.updateAddressBook.phoneNumber").value(updateAddressDto.getPhoneNumber()))
			  .andExpect(jsonPath("$.result.updateAddressBook.email").value(updateAddressDto.getEmail()))
			  .andExpect(jsonPath("$.result.updateAddressBook.address").value(updateAddressDto.getAddress()))
			  .andExpect(jsonPath("$.result.updateAddressBook.name").value(updateAddressDto.getName()))
			  .andDo(print());
	}
}