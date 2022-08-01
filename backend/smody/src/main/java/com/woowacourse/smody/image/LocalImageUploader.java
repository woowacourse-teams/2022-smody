package com.woowacourse.smody.image;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class LocalImageUploader implements ImageUploader {

	private static final String ABSOLUTE_LOCAL_PATH = Paths.get("").toAbsolutePath() + "/images";

	@Value("${image.origin}")
	private String imageOrigin;

	@PostConstruct
	public void initSpace() {
		File folder = new File(ABSOLUTE_LOCAL_PATH);
		folder.mkdir();
	}

	@Override
	public String upload(MultipartFile image, String path, String fileName) {
		if (image.isEmpty()) {
			// todo
		}
		String fullPath = ABSOLUTE_LOCAL_PATH + path;
		File folder = new File(fullPath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		String fileExtension = image.getOriginalFilename()
			.split(".")[1];
		String imageUrl = fullPath + "/" + fileName + "." + fileExtension;

		try {
			image.transferTo(new File(imageUrl));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return imageOrigin + "/" + imageUrl;
	}
}
