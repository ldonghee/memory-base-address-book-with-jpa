package com.hyundai.test.address.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import com.hyundai.test.address.common.utils.CsvFileReader;
import com.hyundai.test.address.common.utils.CsvFileWriter;
import com.hyundai.test.address.domain.AddressBookEntities;
import com.hyundai.test.address.dto.AddressBookDto;
import com.hyundai.test.address.repository.AddressBookJpaRepository;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class AddressBookFileIOService {
	private final AddressBookJpaRepository addressBookJpaRepository;
	private final AddressBookCacheProvider addressBookCacheProvider;
	private final CsvFileReader reader = new CsvFileReader();
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

	public void load(String filePath) {
		try {
			List<String> lines = reader.read(filePath);
			writeToDatabase(toAddressBookDtoList(lines));
		} catch (IOException e) {
			e.printStackTrace();
			log.error("데이터 읽기 실패 : " + e.getMessage());
		}
	}

	private List<AddressBookDto> toAddressBookDtoList(List<String> lines) {
		List<AddressBookDto> addressBookDtoList = new ArrayList<>();
		for (int i = 1; i < lines.size(); i++) {
			String[] values = lines.get(i).split(",");
			addressBookDtoList.add(new AddressBookDto(values[1], values[2], values[0], values[3]));
		}
		return addressBookDtoList;
	}

	private void writeToDatabase(List<AddressBookDto> addressBookDtoList) {
		AddressBookEntities addressBookEntities = new AddressBookEntities(addressBookDtoList);
		addressBookJpaRepository.saveAll(addressBookEntities.getAddressBookEntityList());
		addressBookCacheProvider.addAll(addressBookDtoList);
	}

	public void export(String filePath) {
		List<String> addressBookCsvLines = addressBookCacheProvider.getValues();
		backup(filePath);
		writeToFile(filePath, addressBookCsvLines);
	}

	private void backup(String filePath) {
		try {
			String[] split = filePath.split("\\.");
			String copyFilePath = split[0] + "_" + LocalDateTime.now().format(formatter) + "." + split[1];
			CsvFileWriter.copy(filePath, copyFilePath);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
			log.error("데이터 백업 실패 : " + e.getMessage());
		}
	}

	private void writeToFile(String filePath, List<String> lines) {
		try {
			CsvFileWriter.write(filePath, lines);
		} catch (IOException e) {
			e.printStackTrace();
			log.error("데이터 쓰기 실패 : " + e.getMessage());
		}
	}
}
