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

import com.woowacourse.smody.feed.dto.FeedResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

class FeedAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void setFeeds() {
        LocalDateTime now = LocalDateTime.now();
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID)
            )
        );
        사이클_진행_요청(더즈_토큰,
            ID_추출(
                사이클_생성_요청(더즈_토큰, now, 미라클_모닝_ID)
            )
        );
        사이클_진행_요청(토닉_토큰,
            ID_추출(
                사이클_생성_요청(토닉_토큰, now, 미라클_모닝_ID)
            )
        );
        사이클_진행_요청(알파_토큰,
            ID_추출(
                사이클_생성_요청(알파_토큰, now, 미라클_모닝_ID)
            )
        );
    }

    @DisplayName("피드 전체 조회를 한다.")
    @Test
    void get_feeds() {
        // when
        ExtractableResponse<Response> response = 피드_전체_조회_요청(3, null);

        // then
        List<FeedResponse> actual = toResponseDtoList(response, FeedResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(FeedResponse::getMemberId)
                .containsExactly(알파_ID, 토닉_ID, 더즈_ID)
        );
    }

    @DisplayName("피드 조회를 커서 이후로 한다.")
    @Test
    void get_feeds_cursor() {
        // when
        ExtractableResponse<Response> response = 피드_전체_조회_요청(3, 4L);

        // then
        List<FeedResponse> actual = toResponseDtoList(response, FeedResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(FeedResponse::getMemberId)
                .containsExactly(토닉_ID, 더즈_ID, 조조그린_ID)
        );
    }

    @DisplayName("피드 하나를 조회한다.")
    @Test
    void get_feeds_one() {
        // when
        ExtractableResponse<Response> response = 피드_조회_요청(1L);

        // then
        FeedResponse actual = toResponseDto(response, FeedResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getMemberId()).isEqualTo(조조그린_ID)
        );
    }
}
