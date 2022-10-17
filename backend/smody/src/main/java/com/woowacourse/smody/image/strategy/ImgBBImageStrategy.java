package com.woowacourse.smody.image.strategy;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.dto.ImageUrlResponse;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ImgBBImageStrategy implements ImageStrategy {

    private static final String IMGBB_KEY = "085936a4cafeb33b7d1a80073e9a1f91";

    private final RestTemplate restTemplate;

    @Override
    public String extractUrl(MultipartFile rawImage) {
        validateEmptyImage(rawImage);

        String requestUri = "https://api.imgbb.com/1/upload?key=" + IMGBB_KEY;

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", rawImage.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ImageUrlResponse response = restTemplate
                .postForEntity(requestUri, httpEntity, ImageUrlResponse.class)
                .getBody();
        if (response == null) {
            throw new IllegalStateException("ImgBB 서버로부터 응답을 받아오지 못했습니다.");
        }
        return response.getData().getUrl();
    }

    private void validateEmptyImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new BusinessException(ExceptionData.EMPTY_IMAGE);
        }
    }
}
