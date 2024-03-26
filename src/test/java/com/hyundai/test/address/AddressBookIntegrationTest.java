package com.hyundai.test.address;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hyundai.test.address.config.TestDataProcessorConfig;
import com.hyundai.test.address.dto.AddressBookUpdateRequest;

@ActiveProfiles("test")
@Import(TestDataProcessorConfig.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class AddressBookIntegrationTest {
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper objectMapper;

	@Test
	@DisplayName("필터/정렬 파라미터에 따른 주소록 조회")
	public void get_address_books() throws Exception {
		// given & when
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
			  .andExpect(jsonPath("$.result[0].phoneNumber").value("010-000-0001"))
			  .andExpect(jsonPath("$.result[0].email").value("lee@hyundai.com"))
			  .andExpect(jsonPath("$.result[0].address").value("경기도 성남시"))
			  .andExpect(jsonPath("$.result[0].name").value("이몽룡"))
			  .andExpect(jsonPath("$.result[1].phoneNumber").value("010-000-0004"))
			  .andExpect(jsonPath("$.result[1].email").value("lee3@hyundai.com"))
			  .andExpect(jsonPath("$.result[1].address").value("전라북도 남원시"))
			  .andExpect(jsonPath("$.result[1].name").value("이몽룡"))
			  .andDo(print());
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 삭제")
	void delete_address_book() throws Exception {
		// given & when
		ResultActions result = mockMvc.perform(delete("/address-books/{phone_number}", "01000000002"))
									  .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result.phoneNumber").value("01000000002"))
			  .andExpect(jsonPath("$.result.email").value("lee2@hyundai.com"))
			  .andExpect(jsonPath("$.result.address").value("강원도 강릉시"))
			  .andExpect(jsonPath("$.result.name").value("이순신"))
			  .andDo(print());
	}

	@Test
	@DisplayName("이름 파라미터 값을 포함하는 주소록 삭제")
	void delete_address_book_by_name() throws Exception {
		// given & when
		ResultActions result = mockMvc.perform(delete("/address-books").param("name", "테스"))
									  .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result[0].phoneNumber").value("010-0001-0001"))
			  .andExpect(jsonPath("$.result[0].email").value("test1@hyundai.com"))
			  .andExpect(jsonPath("$.result[0].address").value("서울시 금천구"))
			  .andExpect(jsonPath("$.result[0].name").value("테스트"))
			  .andExpect(jsonPath("$.result[1].phoneNumber").value("010-0001-0002"))
			  .andExpect(jsonPath("$.result[1].email").value("test2@hyundai.com"))
			  .andExpect(jsonPath("$.result[1].address").value("경기도 용인시"))
			  .andExpect(jsonPath("$.result[1].name").value("테스트"))
			  .andDo(print());
	}

	@Test
	@DisplayName("전화번호 파라미터 값에 해당하는 주소록 수정")
	void update_address_book() throws Exception {
		// given
		AddressBookUpdateRequest updateRequest = new AddressBookUpdateRequest("01000000000", "test@hyundai.com", "서울시 금천구", "테스트");
		// when
		ResultActions result =
				mockMvc.perform(put("/address-books/{phone_number}", "01000000000")
										.contentType(MediaType.APPLICATION_JSON)
										.content(objectMapper.writeValueAsString(updateRequest)))
					   .andDo(print());
		// then
		result.andExpect(status().isOk())
			  .andExpect(content().contentType(MediaType.APPLICATION_JSON))
			  .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
			  .andExpect(jsonPath("$.statusMessage").value(HttpStatus.OK.getReasonPhrase()))
			  .andExpect(jsonPath("$.result.beforeAddressBook.phoneNumber").value("01000000000"))
			  .andExpect(jsonPath("$.result.beforeAddressBook.email").value("hong@hyundai.com"))
			  .andExpect(jsonPath("$.result.beforeAddressBook.address").value("서울시 광진구"))
			  .andExpect(jsonPath("$.result.beforeAddressBook.name").value("홍길동"))
			  .andExpect(jsonPath("$.result.updateAddressBook.phoneNumber").value(updateRequest.getPhoneNumber()))
			  .andExpect(jsonPath("$.result.updateAddressBook.email").value(updateRequest.getEmail()))
			  .andExpect(jsonPath("$.result.updateAddressBook.address").value(updateRequest.getAddress()))
			  .andExpect(jsonPath("$.result.updateAddressBook.name").value(updateRequest.getName()))
			  .andDo(print());
	}
}
