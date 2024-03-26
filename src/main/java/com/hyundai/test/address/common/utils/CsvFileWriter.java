package com.hyundai.test.address.common.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CsvFileWriter {
	public static void write(String filePath, List<String> list) throws IOException {
		File csv = new File(filePath);
		BufferedWriter bw = null;
		try {
			bw = new BufferedWriter(new FileWriter(csv, false));
			for (String s : list) {
				bw.write(s);
				bw.newLine();
			}
		} finally {
			if (bw != null) {
				bw.flush();
				bw.close();
			}
		}
	}

	public static void copy(String filePath, String backFilePath) throws IOException, URISyntaxException {
		Path source = Paths.get(filePath);
		Path target = Paths.get(backFilePath);

		if (!Files.exists(source)) {
			throw new FileNotFoundException("파일이 존재하지 않습니다.");
		}

		if (!Files.exists(target.getParent())) {
			Files.createDirectories(target.getParent());
		}

		Files.copy(source, target, StandardCopyOption.REPLACE_EXISTING);
		log.info("File 복사 완료 : " + target.getFileName());
	}
}
