package com.woowacourse.smody.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

public class CommentTest {

    @DisplayName("댓글 생성 시 content의 길이가 1자 미만 255자 초과일 경우 예외를 발생시킨다.")
    @Test
    void newComment_InvalidContent() {
        // given
        String invalidContent = "1234567890".repeat(25) + "123456";
        Member member = new Member("email@email.com", "nickname", "introduction", "picture");
        Challenge challenge = new Challenge("challenge");
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now());
        Image image = new Image(new MockMultipartFile(
                "progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
        ), i -> "image.jpg");
        cycle.increaseProgress(LocalDateTime.now().plusMinutes(1), image, "인증1");
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

        // when, then
        assertThatThrownBy(() -> new Comment(cycleDetail, member, invalidContent))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_COMMENT_CONTENT);
    }
}
