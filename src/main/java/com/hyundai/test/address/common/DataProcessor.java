package com.hyundai.test.address.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.hyundai.test.address.service.AddressBookFileIOService;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;

@Component
@RequiredArgsConstructor
public class DataProcessor {
	private final AddressBookFileIOService addressBookFileService;
	@Value("${app.file.load.path}")
	private String FILE_PATH;
	@Value("${app.file.dest.path}")
	private String DEST_FILE_PATH;

	@PostConstruct
	public void init() {
		addressBookFileService.load(FILE_PATH);
	}

	@PreDestroy
	public void destroy() {
		addressBookFileService.export(DEST_FILE_PATH);
	}
}
