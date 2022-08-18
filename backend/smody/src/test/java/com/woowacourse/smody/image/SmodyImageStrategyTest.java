package com.woowacourse.smody.image;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.image.strategy.SmodyImageStrategy;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class SmodyImageStrategyTest {

    @DisplayName("이미지의 바이트코드가 비었을 때 예외를 발생")
    @Test
    void upload_emptyImage() {
        // given
        MultipartFile emptyImageFile = new MockMultipartFile(
                "image", "image.jpg", "image/jpg", "".getBytes()
        );
        ImageStrategy smodyImageStrategy = new SmodyImageStrategy();

        // when then
        assertThatThrownBy(
                () -> smodyImageStrategy.extractUrl(emptyImageFile)
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
        ImageStrategy smodyImageStrategy = new SmodyImageStrategy();

        // when then
        assertThatThrownBy(
                () -> smodyImageStrategy.extractUrl(emptyImageFile)
        ).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.EMPTY_IMAGE);
    }
}
