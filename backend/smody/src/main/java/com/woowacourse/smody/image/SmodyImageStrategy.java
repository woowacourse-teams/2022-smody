package com.woowacourse.smody.image;

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
public class SmodyImageStrategy implements ImageStrategy {

    @Override
    public String extractUrl(MultipartFile rawImage) {
        validateEmptyImage(rawImage);

        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("rawImage", rawImage.getResource());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(body, headers);
        return new RestTemplate()
                .postForEntity("https://images.smody.co.kr/images/upload", httpEntity, String.class)
                .getBody();
    }

    private void validateEmptyImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new BusinessException(ExceptionData.EMPTY_IMAGE);
        }
    }
}
