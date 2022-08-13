package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.dto.CommentRequest;
import com.woowacourse.smody.dto.CommentResponse;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CommentQueryServiceTest extends IntegrationTest {

    @Autowired
    private CommentQueryService commentQueryService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private ResourceFixture resourceFixture;

    @DisplayName("cycleDetaild의 댓글을 조회한다.")
    @Test
    void findAllByCycle() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, now);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Long 댓글1 = commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글1"));
        Long 댓글2 = commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(),
                new CommentRequest("댓글2"));
        // when
        List<CommentResponse> commentResponses = commentQueryService.findAllByCycleDetailId(
                new TokenPayload(0L), cycleDetail.getId());
        // then
        assertThat(commentResponses.stream()
                .map(CommentResponse::getCommentId)
                .collect(Collectors.toList())).containsExactly(댓글1, 댓글2);
    }

}
