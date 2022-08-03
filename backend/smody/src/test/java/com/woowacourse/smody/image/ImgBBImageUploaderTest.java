package com.woowacourse.smody.image;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;

class ImgBBImageUploaderTest {

    @DisplayName("이미지의 바이트코드가 비었을 때 예외를 발생")
    @Test
    void upload_emptyImage() {
        // given
        MultipartFile emptyImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpg", "".getBytes()
        );
        ImageUploader imgBBImageUploader = new ImgBBImageUploader();

        // when then
        assertThatThrownBy(
                () ->imgBBImageUploader.upload(emptyImageFile, "/path", "fileName.jpg")
        ).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.EMPTY_IMAGE);
    }

    @DisplayName("이미지의 바이트코드가 null일 때 예외를 발생")
    @Test
    void upload_nullImage() {
        // given
        MultipartFile emptyImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpg", (byte[]) null
        );
        ImageUploader imgBBImageUploader = new ImgBBImageUploader();

        // when then
        assertThatThrownBy(
                () ->imgBBImageUploader.upload(emptyImageFile, "/path", "fileName.jpg")
        ).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.EMPTY_IMAGE);
    }
}
