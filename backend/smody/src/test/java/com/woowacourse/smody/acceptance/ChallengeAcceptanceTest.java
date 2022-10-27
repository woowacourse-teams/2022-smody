package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.challenge.dto.ChallengersResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class ChallengeAcceptanceTest extends AcceptanceTest {

    @DisplayName("전체 챌린지를 페이징하여 조회한다.")
    @Test
    void get_challenges() {
        // when
        ExtractableResponse<Response> response = 챌린지_조회_요청(2, null, null, null);

        // then
        List<ChallengeTabResponse> actual = toResponseDtoList(response, ChallengeTabResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual)
                .map(ChallengeTabResponse::getChallengeId)
                .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID)
        );
    }

    @DisplayName("커서 이후의 챌린지를 조회한다.")
    @Test
    void get_challenges_cursor() {
        // when
        ExtractableResponse<Response> response = 챌린지_조회_요청(2, null, 3L, null);

        // then
        List<ChallengeTabResponse> actual = toResponseDtoList(response, ChallengeTabResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual)
                .map(ChallengeTabResponse::getChallengeId)
                .containsExactly(알고리즘_풀기_ID, JPA_공부_ID)
        );
    }

    @DisplayName("챌린지를 검색한다.")
    @Test
    void get_challenge_filter() {
        // when
        ExtractableResponse<Response> response = 챌린지_조회_요청(2, null, null, "오늘의");

        // then
        List<ChallengeTabResponse> actual = toResponseDtoList(response, ChallengeTabResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(1),
            () -> assertThat(actual)
                .map(ChallengeTabResponse::getChallengeId)
                .containsExactly(오늘의_운동_ID)
        );
    }

    @DisplayName("인기 순으로 챌린지를 조회한다.")
    @Test
    void get_challenges_sort_popular() {
        // given
        사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID);
        사이클_생성_요청(더즈_토큰, LocalDateTime.now(), 미라클_모닝_ID);

        사이클_생성_요청(토닉_토큰, LocalDateTime.now(), 스모디_방문하기_ID);

        // when
        ExtractableResponse<Response> response = 챌린지_조회_요청(2, "popular", null, null);

        // then
        List<ChallengeTabResponse> actual = toResponseDtoList(response, ChallengeTabResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual).hasSize(2),
            () -> assertThat(actual)
                .map(ChallengeTabResponse::getChallengeId)
                .containsExactly(미라클_모닝_ID, 스모디_방문하기_ID),
            () -> assertThat(actual)
                .map(ChallengeTabResponse::getChallengerCount)
                .containsExactly(2, 1)
        );
    }

    @DisplayName("나의 챌린지들을 조회한다.")
    @Test
    void get_challenges_me() {
        // given
        사이클_생성_요청(더즈_토큰, LocalDateTime.now(), 오늘의_운동_ID);

        사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 스모디_방문하기_ID);
        사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID);

        // when
        ExtractableResponse<Response> response = 나의_챌린지_조회_요청(
            조조그린_토큰, 5, null, null
        );

        // then
        List<ChallengeOfMineResponse> actual = toResponseDtoList(response, ChallengeOfMineResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(ChallengeOfMineResponse::getChallengeId)
                .containsOnly(미라클_모닝_ID, 스모디_방문하기_ID)
        );
    }

    @DisplayName("나의 특정 챌린지를 조회한다.")
    @Test
    void get_challenges_me_one() {
        // given
        LocalDateTime now = LocalDateTime.now();
        사이클_진행_요청(
            조조그린_토큰,
            ID_추출(사이클_생성_요청(조조그린_토큰, now, 미라클_모닝_ID))
        );

        // when
        ExtractableResponse<Response> response = 나의_챌린지_조회_요청(조조그린_토큰, 미라클_모닝_ID);

        // then
        ChallengeHistoryResponse actual = toResponseDto(response, ChallengeHistoryResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getChallengeName()).isEqualTo("미라클 모닝"),
            () -> assertThat(actual.getCycleDetailCount()).isEqualTo(1),
            () -> assertThat(actual.getSuccessCount()).isEqualTo(0)
        );
    }

    @DisplayName("하나의 챌린지를 조회한다.")
    @Test
    void get_challenges_one() {
        // when
        ExtractableResponse<Response> response = 챌린지_조회_요청(스모디_방문하기_ID);

        // then
        ChallengeResponse actual = toResponseDto(response, ChallengeResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getChallengeId()).isEqualTo(스모디_방문하기_ID)
        );
    }

    @DisplayName("챌린지 참가자를 조회한다.")
    @Test
    void get_challenges_challengers() {
        // given
        사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID);
        사이클_생성_요청(더즈_토큰, LocalDateTime.now(), 미라클_모닝_ID);
        사이클_생성_요청(알파_토큰, LocalDateTime.now(), 미라클_모닝_ID);

        사이클_생성_요청(토닉_토큰, LocalDateTime.now(), 스모디_방문하기_ID);

        // when
        ExtractableResponse<Response> response = 챌린저_조회_요청(미라클_모닝_ID);

        // then
        List<ChallengersResponse> actual = toResponseDtoList(response, ChallengersResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(ChallengersResponse::getMemberId)
                .containsOnly(조조그린_ID, 더즈_ID, 알파_ID)
        );
    }

    @DisplayName("챌린지를 생성한다.")
    @Test
    void post_challenge() {
        // when
        ExtractableResponse<Response> response = 챌린지_생성_요청(
            조조그린_토큰, "테스트 작성하기", "테스트를 열심히"
        );

        // then
        assertAll(
            CREATED_응답(response),
            () -> assertThat(ID_추출(response)).isEqualTo(6L)
        );
    }
}
