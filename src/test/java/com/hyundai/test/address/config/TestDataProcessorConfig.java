package com.hyundai.test.address.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.hyundai.test.address.common.DataProcessor;
import com.hyundai.test.address.service.AddressBookFileIOService;

import jakarta.annotation.PreDestroy;

@TestConfiguration
public class TestDataProcessorConfig {
	private final AddressBookFileIOService addressBookFileService;

	public TestDataProcessorConfig(AddressBookFileIOService addressBookFileService) {
		this.addressBookFileService = addressBookFileService;
	}

	@Bean
	public DataProcessor dataProcessor() {
		return new DataProcessor(addressBookFileService) {
			@PreDestroy
			public void destroy() {
			}
		};
	}
}
