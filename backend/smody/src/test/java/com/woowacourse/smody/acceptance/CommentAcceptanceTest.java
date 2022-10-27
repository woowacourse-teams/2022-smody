package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.comment.dto.CommentResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class CommentAcceptanceTest extends AcceptanceTest {

    private Long 피드_ID;

    @BeforeEach
    void setFeed() {
        LocalDateTime now = LocalDateTime.now();
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID)
            )
        );
        피드_ID = 1L;
    }

    @DisplayName("댓글을 작성한다.")
    @Test
    void post_comments() {
        // when
        ExtractableResponse<Response> response = 댓글_작성_요청(더즈_토큰, 피드_ID, "댓글 내용");

        // then
        assertAll(
            CREATED_응답(response),
            () -> assertThat(ID_추출(response)).isEqualTo(1L)
        );
    }

    @DisplayName("댓글을 수정한다.")
    @Test
    void patch_comments() {
        // given
        Long 댓글_ID = ID_추출(
            댓글_작성_요청(더즈_토큰, 피드_ID, "댓글 내용")
        );

        // when
        ExtractableResponse<Response> response = 댓글_수정_요청(더즈_토큰, 댓글_ID, "수정 댓글");

        // then
        List<CommentResponse> actual = toResponseDtoList(댓글_조회_요청(피드_ID), CommentResponse.class);
        assertAll(
            NO_CONTENT_응답(response),
            () -> assertThat(actual.get(0).getContent()).isEqualTo("수정 댓글")
        );
    }

    @DisplayName("댓글을 삭제한다.")
    @Test
    void delete_comments() {
        // given
        Long 댓글_ID = ID_추출(
            댓글_작성_요청(더즈_토큰, 피드_ID, "댓글 내용")
        );

        // when
        ExtractableResponse<Response> response = 댓글_삭제_요청(더즈_토큰, 댓글_ID);

        // then
        List<CommentResponse> actual = toResponseDtoList(댓글_조회_요청(피드_ID), CommentResponse.class);
        assertAll(
            NO_CONTENT_응답(response),
            () -> assertThat(actual).isEmpty()
        );
    }

    @DisplayName("비회원이 피드의 댓글을 조회한다.")
    @Test
    void get_comments() {
        // given
        댓글_작성_요청(더즈_토큰, 피드_ID, "댓글 내용");
        댓글_작성_요청(토닉_토큰, 피드_ID, "댓글 내용");
        댓글_작성_요청(알파_토큰, 피드_ID, "댓글 내용");

        // when
        ExtractableResponse<Response> response = 댓글_조회_요청(피드_ID);

        // then
        List<CommentResponse> actual = toResponseDtoList(response, CommentResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(CommentResponse::getMemberId)
                .containsExactly(더즈_ID, 토닉_ID, 알파_ID),
            () -> assertThat(actual)
                .map(CommentResponse::getIsMyComment)
                .containsExactly(false, false, false)
        );
    }

    @DisplayName("로그인 후 피드의 댓글을 조회한다.")
    @Test
    void get_comments_auth() {
        // given
        댓글_작성_요청(더즈_토큰, 피드_ID, "댓글 내용");
        댓글_작성_요청(토닉_토큰, 피드_ID, "댓글 내용");
        댓글_작성_요청(알파_토큰, 피드_ID, "댓글 내용");

        // when
        ExtractableResponse<Response> response = 로그인_댓글_조회_요청(더즈_토큰, 피드_ID);

        // then
        List<CommentResponse> actual = toResponseDtoList(response, CommentResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(CommentResponse::getMemberId)
                .containsExactly(더즈_ID, 토닉_ID, 알파_ID),
            () -> assertThat(actual)
                .map(CommentResponse::getIsMyComment)
                .containsExactly(true, false, false)
        );
    }
}
