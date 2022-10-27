package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.cycle.dto.CycleResponse;
import com.woowacourse.smody.cycle.dto.FilteredCycleHistoryResponse;
import com.woowacourse.smody.cycle.dto.InProgressCycleResponse;
import com.woowacourse.smody.cycle.dto.ProgressResponse;
import com.woowacourse.smody.cycle.dto.StatResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class CycleAcceptanceTest extends AcceptanceTest {

    @DisplayName("사이클을 생성한다.")
    @Test
    void post_cycles() {
        // given
        String token = 토큰_요청(조조그린_ID);

        // when
        ExtractableResponse<Response> response = 사이클_생성_요청(token, LocalDateTime.now(), 미라클_모닝_ID);

        // then
        assertAll(
            CREATED_응답(response),
            () -> assertThat(ID_추출(response)).isEqualTo(1L)
        );
    }

    @DisplayName("사이클 진척도를 증가시킨다.")
    @Test
    void post_cycle_progress() {
        // given
        String 조조그린_토큰 = 토큰_요청(조조그린_ID);
        Long 사이클_ID = ID_추출(
            사이클_생성_요청(
                조조그린_토큰,
                LocalDateTime.now(),
                미라클_모닝_ID
            ));

        // when
        ExtractableResponse<Response> response = 사이클_진행_요청(조조그린_토큰, 사이클_ID);

        // then
        ProgressResponse actual = toResponseDto(response, ProgressResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getProgressCount()).isEqualTo(1)
        );
    }

    @DisplayName("나의 진행 중인 모든 사이클을 조회한다.")
    @Test
    void get_cycles_me() {
        // given
        String 조조그린_토큰 = 토큰_요청(조조그린_ID);

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime 삼일전 = now.minusDays(3).minusMinutes(1);

        사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID);
        사이클_생성_요청(조조그린_토큰, now, 스모디_방문하기_ID);
        사이클_생성_요청(조조그린_토큰, now, 오늘의_운동_ID);

        사이클_생성_요청(조조그린_토큰, 삼일전, JPA_공부_ID);
        사이클_생성_요청(토큰_요청(더즈_ID), now, JPA_공부_ID);

        // when
        ExtractableResponse<Response> response = 나의_사이클_조회_요청(조조그린_토큰);

        // then
        List<InProgressCycleResponse> actual = toResponseDtoList(response, InProgressCycleResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(3),
            () -> assertThat(actual)
                .map(InProgressCycleResponse::getChallengeId)
                .containsOnly(미라클_모닝_ID, 스모디_방문하기_ID, 오늘의_운동_ID)
        );
    }

    @DisplayName("하나의 사이클을 조회한다.")
    @Test
    void get_cycles_one() {
        // given
        Long 사이클_ID = ID_추출(
            사이클_생성_요청(
                토큰_요청(조조그린_ID),
                LocalDateTime.now(),
                미라클_모닝_ID
            ));

        // when
        ExtractableResponse<Response> response = 사이클_조회_요청(사이클_ID);

        // then
        CycleResponse actual = toResponseDto(response, CycleResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getCycleId()).isEqualTo(사이클_ID)
        );
    }

    @DisplayName("사이클 통계 정보를 조회한다.")
    @Test
    void get_cycles_me_stat() {
        // given
        String 조조그린_토큰 = 토큰_요청(조조그린_ID);
        LocalDateTime now = LocalDateTime.now();

        사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID);
        사이클_생성_요청(조조그린_토큰, now, 스모디_방문하기_ID);

        사이클_생성_요청(토큰_요청(더즈_ID), now, 오늘의_운동_ID);

        // when
        ExtractableResponse<Response> response = 사이클_통계_요청(조조그린_토큰);

        // then
        StatResponse actual = toResponseDto(response, StatResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getTotalCount()).isEqualTo(2),
            () -> assertThat(actual.getSuccessCount()).isEqualTo(0)
        );
    }

    @DisplayName("나의 한 챌린지에 대한 사이클들을 조회한다.")
    @Test
    void get_cycles_me_challengeId() {
        // given
        String 조조그린_토큰 = 토큰_요청(조조그린_ID);
        LocalDateTime now = LocalDateTime.now();

        사이클_생성_요청(조조그린_토큰, now.minusDays(4), 미라클_모닝_ID);
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID)
            )
        );

        사이클_생성_요청(조조그린_토큰, now, 스모디_방문하기_ID);

        // when
        ExtractableResponse<Response> response = 챌린지별_사이클_조회_요청(조조그린_토큰, 미라클_모닝_ID);

        // then
        List<FilteredCycleHistoryResponse> actual = toResponseDtoList(response, FilteredCycleHistoryResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual)
                .map(res -> res.getCycleDetails().size())
                .containsOnly(1, 0)
        );
    }
}
