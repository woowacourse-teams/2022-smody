package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.support.ResourceFixture;
import io.restassured.RestAssured;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

public class CommentAcceptanceTest extends AcceptanceTest {

    @Autowired
    private ResourceFixture resourceFixture;

    @Autowired
    private CommentRepository commentRepository;

    @DisplayName("댓글을 수정한다.")
    @Test
    void updateComment() {
        Cycle cycle = resourceFixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        Member member = cycle.getMember();
        String token = 로그인_혹은_회원가입(member);
        CycleDetail cycleDetail = cycle.getCycleDetails().get(0);
        Comment comment = new Comment(cycleDetail, member, "수정전");
        commentRepository.save(comment);
        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(new CommentUpdateRequest("수정후"))
                .when()
                .patch("/comments/" + comment.getId())
                .then().log().all()
                .extract();
        Comment actual = commentRepository.findById(comment.getId()).get();
        assertThat(actual.getContent()).isEqualTo("수정후");
    }
}
