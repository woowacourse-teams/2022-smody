package com.woowacourse.smody.image.domain;

import com.woowacourse.smody.image.strategy.ImageStrategy;
import lombok.RequiredArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
public class Image {

    private final MultipartFile multipartFile;

    private final ImageStrategy imageStrategy;

    private String url;

    public String getUrl() {
        if (url == null) {
            this.url = imageStrategy.extractUrl(multipartFile);
        }
        return this.url;
    }
}
