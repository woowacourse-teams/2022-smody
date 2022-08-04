package com.woowacourse.smody.image;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStrategy {

    String extractUrl(MultipartFile image);
}
