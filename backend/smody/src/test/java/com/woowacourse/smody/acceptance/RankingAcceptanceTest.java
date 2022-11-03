package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RankingAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    void progressCycle() {
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID)
            )
        );

        사이클_진행_요청(토닉_토큰,
            ID_추출(
                사이클_생성_요청(토닉_토큰, LocalDateTime.now(), 미라클_모닝_ID)
            )
        );

        // given
        사이클_진행_요청(더즈_토큰,
            ID_추출(
                사이클_생성_요청(더즈_토큰, LocalDateTime.now(), 미라클_모닝_ID)
            )
        );
        사이클_진행_요청(더즈_토큰,
            ID_추출(
                사이클_생성_요청(더즈_토큰, LocalDateTime.now(), 오늘의_운동_ID)
            )
        );
    }

    @DisplayName("랭킹 기간을 조회한다.")
    @Test
    void get_ranking_periods() {
        // when
        ExtractableResponse<Response> response = 랭킹_기간_조회_요청();

        // then
        List<RankingPeriodResponse> actual = toResponseDtoList(response, RankingPeriodResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(1)
        );
    }

    @DisplayName("랭킹을 조회한다.")
    @Test
    void get_ranking_activities() {
        // when
        ExtractableResponse<Response> response = 랭킹_활동_조회_요청(1L);

        // then
        List<RankingActivityResponse> actual = toResponseDtoList(response, RankingActivityResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(RankingActivityResponse::getMemberId)
                .containsExactly(더즈_ID, 조조그린_ID, 토닉_ID),
            () -> assertThat(actual)
                .map(RankingActivityResponse::getRanking)
                .containsExactly(1, 2, 2)
        );
    }

    @DisplayName("나의 랭킹을 조회한다.")
    @Test
    void get_ranking_activities_me() {
        // when
        ExtractableResponse<Response> response = 나의_랭킹_활동_조회_요청(조조그린_토큰, 1L);

        // then
        RankingActivityResponse actual = toResponseDto(response, RankingActivityResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getMemberId()).isEqualTo(조조그린_ID),
            () -> assertThat(actual.getRanking()).isEqualTo(2)
        );
    }
}
