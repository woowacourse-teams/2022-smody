package com.woowacourse.smody.util;

import org.springframework.web.multipart.MultipartFile;

public interface ImageUploader {

    String upload(MultipartFile image, String path, String fileName);

    boolean remove(String imageUrl);
}
