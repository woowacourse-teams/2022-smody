package com.woowacourse.smody.image;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.nio.file.Paths;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class LocalImageUploaderTest {

	private static final String ABSOLUTE_LOCAL_PATH = Paths.get("").toAbsolutePath().toString();

	private final ImageUploader imageUploader = new LocalImageUploader("localhost:8080");

	@AfterEach
	void clear() {
		new File(ABSOLUTE_LOCAL_PATH + "/images/test_folder/test_profile.jpg").delete();
		new File(ABSOLUTE_LOCAL_PATH + "/images/test_folder").delete();
		new File(ABSOLUTE_LOCAL_PATH + "/images").delete();
	}

	@DisplayName("이미지를 로컬에 업로드 한다.")
	@Test
	void upload() {
		// given
		MultipartFile image = new MockMultipartFile(
			"profileImage", "profile.jpg", "image/jpg", "image".getBytes()
		);

		// when
		String imageUrl = imageUploader.upload(image, "test_folder", "test_profile");

		// then
		assertThat(imageUrl).isEqualTo("localhost:8080/images/test_folder/test_profile.jpg");
		assertThat(new File(ABSOLUTE_LOCAL_PATH + "/images/test_folder/test_profile.jpg").exists())
			.isTrue();
	}

	@DisplayName("이미지를 로컬에서 삭제한다.")
	@Test
	void remove() {
		// given
		MultipartFile image = new MockMultipartFile(
			"profileImage", "profile.jpg", "image/jpg", "image".getBytes()
		);
		String imageUrl = imageUploader.upload(image, "test_folder", "test_profile");

		// when
		imageUploader.remove(imageUrl);

		// then
		assertThat(new File(ABSOLUTE_LOCAL_PATH + "/images/test_folder/test_profile.jpg").exists())
			.isFalse();
	}
}
