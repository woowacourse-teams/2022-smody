package com.woowacourse.smody.comment.domain;

import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CommentTest {

    @DisplayName("댓글 생성 시 content의 길이가 1자 미만 255자 초과일 경우 예외를 발생시킨다.")
    @Test
    void newComment_InvalidContent() {
        // given
        String invalidContent = "1234567890".repeat(25) + "123456";
        Member member = new Member("email@email.com", "nickname", "introduction", "picture");
        Challenge challenge = new Challenge("challenge", "설명", 1, 1);
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now());
        cycle.increaseProgress(LocalDateTime.now().plusMinutes(1), 이미지, "인증1");
        CycleDetail cycleDetail = cycle.getCycleDetailsOrderByProgress().get(0);

        // when, then
        assertThatThrownBy(() -> new Comment(cycleDetail, member, invalidContent))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_COMMENT_CONTENT);
    }
}
