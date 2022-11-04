package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class PushAcceptanceTest extends AcceptanceTest {

    private final String endpoint = "endpoint";

    @DisplayName("알림 구독을 한다.")
    @Test
    void post_subscribe() {
        // when
        ExtractableResponse<Response> response = 알림_구독_요청(조조그린_토큰, endpoint);

        // then
        assertAll(
            OK_응답(response)
        );
    }

    @DisplayName("알림 구독을 해제한다.")
    @Test
    void post_unsubscribe() {
        // given
        알림_구독_요청(조조그린_토큰, endpoint);

        // when
        ExtractableResponse<Response> response = 알림_구독_해제_요청(조조그린_토큰, endpoint);

        // then
        assertAll(
            NO_CONTENT_응답(response)
        );
    }

    @DisplayName("피드에서 멘션 알림을 저장한다.")
    @Test
    void post_push_notifications() {
        // given
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID)
            )
        );
        Long 피드_ID = 1L;

        // when
        ExtractableResponse<Response> response = 맨션_알림_요청(
            조조그린_토큰,
            List.of(더즈_ID, 토닉_ID, 알파_ID),
            피드_ID
        );

        // then
        assertAll(
            OK_응답(response)
        );
    }

    @DisplayName("나의 알림을 조회한다.")
    @Test
    void get_push_notifications() {
        // when
        ExtractableResponse<Response> response = 나의_알림_조회_요청(조조그린_토큰);

        // then
        assertAll(
            OK_응답(response)
        );
    }

    @DisplayName("알림을 하나 삭제한다.")
    @Test
    void delete_push_notifications() {
        // given
        Long 피드_ID = 1L;
        맨션_알림_요청(
            더즈_토큰,
            List.of(조조그린_ID),
            피드_ID
        );

        // when
        ExtractableResponse<Response> response = 알림_삭제_요청(조조그린_토큰, 1L);

        // then
        assertAll(
            NO_CONTENT_응답(response)
        );
    }

    @DisplayName("알림을 하나 삭제한다.")
    @Test
    void delete_push_notifications_me() {
        // when
        ExtractableResponse<Response> response = 나의_알림_삭제_요청(조조그린_토큰);

        // then
        assertAll(
            NO_CONTENT_응답(response)
        );
    }
}
