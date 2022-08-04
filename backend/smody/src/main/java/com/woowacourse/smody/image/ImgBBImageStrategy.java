package com.woowacourse.smody.image;

import com.woowacourse.smody.dto.ImageUrlResponse;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
@Primary
public class ImgBBImageStrategy implements ImageStrategy {

    private static final String ImgBB_KEY = "085936a4cafeb33b7d1a80073e9a1f91";

    @Override
    public String extractUrl(MultipartFile image) {
        validateEmptyImage(image);
        String requestUri = "https://api.imgbb.com/1/upload?key=" + ImgBB_KEY;
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("image", image.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        ImageUrlResponse response = new RestTemplate().postForEntity(requestUri,
                httpEntity, ImageUrlResponse.class).getBody();

        return response.getData().getUrl();
    }

    private void validateEmptyImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new BusinessException(ExceptionData.EMPTY_IMAGE);
        }
    }
}