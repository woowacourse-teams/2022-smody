package com.woowacourse.smody.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

	String upload(MultipartFile image, String path, String fileName);

	boolean remove(String imageUrl);
}
