package com.woowacourse.smody.image.strategy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Component
@Primary
public class SmodyImageStrategy implements ImageStrategy {

    @Value("${secret.key.upload}")
    private String secretKeyUpload;

    @Override
    public String extractUrl(MultipartFile rawImage) {
        validateEmptyImage(rawImage);
        ResponseEntity<String> imageServerResponse = new RestTemplate()
                .postForEntity("https://images.smody.co.kr/images/upload", generateImageRequest(rawImage),
                        String.class);
        if (imageServerResponse.getStatusCode().is4xxClientError()) {
            throw new BusinessException(ExceptionData.FORBIDDEN_IMAGE_SERVER);
        }
        return imageServerResponse.getBody();
    }

    private void validateEmptyImage(MultipartFile image) {
        if (image.isEmpty()) {
            throw new BusinessException(ExceptionData.EMPTY_IMAGE);
        }
    }

    private HttpEntity<MultiValueMap<String, Object>> generateImageRequest(final MultipartFile rawImage) {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("rawImage", rawImage.getResource());
        body.add("secretKeyUpload", secretKeyUpload);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        return new HttpEntity<>(body, headers);
    }
}
