package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.woowacourse.smody.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import static com.woowacourse.smody.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.ResourceFixture.알파_ID;
import static com.woowacourse.smody.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

class ChallengeServiceTest extends IntegrationTest {

    @Autowired
    private ChallengeQueryService challengeQueryService;

    @Autowired
    private ChallengeService challengeService;

    private final LocalDateTime now = LocalDateTime.now();

    @DisplayName("나의 모든 챌린지 중 가장 최근에 성공한 순으로 챌린지를")
    @Nested
    class SearchSuccessOfMineTest {

        private TokenPayload tokenPayload;

        @BeforeEach
        void setUp() {
            tokenPayload = new TokenPayload(조조그린_ID);

            fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.NOTHING, now);
            fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.FIRST, now.minusDays(3L));
            fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.SUCCESS, now.minusDays(6L));
            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.SUCCESS, now.minusDays(3L));
            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.SUCCESS, now.minusDays(6L));
            fixture.사이클_생성(조조그린_ID, 오늘의_운동_ID, Progress.SUCCESS, now.minusDays(9L));
            fixture.사이클_생성(조조그린_ID, JPA_공부_ID, Progress.SUCCESS, now.minusDays(20L));
            fixture.사이클_생성(조조그린_ID, JPA_공부_ID, Progress.SUCCESS, now.minusDays(23L));
            fixture.사이클_생성(조조그린_ID, JPA_공부_ID, Progress.SUCCESS, now.minusDays(26L));
            fixture.사이클_생성(조조그린_ID, 알고리즘_풀기_ID, Progress.SUCCESS, now.minusDays(30L));
        }

        @DisplayName("조회")
        @Test
        void searchSuccessOfMine() {
            // when
            List<SuccessChallengeResponse> responses = challengeQueryService.searchSuccessOfMine(
                    tokenPayload, PageRequest.of(0, 10));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(5),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getSuccessCount)
                            .containsExactly(2, 1, 1, 3, 1),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getChallengeId)
                            .containsExactly(미라클_모닝_ID, 스모디_방문하기_ID, 오늘의_운동_ID, JPA_공부_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("0페이지의 3개만 조회")
        @Test
        void searchSuccessOfMine_pageFullSize() {
            // when
            List<SuccessChallengeResponse> responses = challengeQueryService.searchSuccessOfMine(
                    tokenPayload, PageRequest.of(0, 3));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(3),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getSuccessCount)
                            .containsExactly(2, 1, 1),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getChallengeId)
                            .containsExactly(미라클_모닝_ID, 스모디_방문하기_ID, 오늘의_운동_ID)
            );
        }

        @DisplayName("1페이지의 2개만 조회")
        @Test
        void searchSuccessOfMine_pagePartialSize() {
            // when
            List<SuccessChallengeResponse> responses = challengeQueryService.searchSuccessOfMine(
                    tokenPayload, PageRequest.of(1, 3));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(2),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getSuccessCount)
                            .containsExactly(3, 1),
                    () -> assertThat(responses)
                            .map(SuccessChallengeResponse::getChallengeId)
                            .containsExactly(JPA_공부_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("없는 페이지로 조회")
        @Test
        void searchSuccessOfMine_pageOverMaxPage() {
            // when
            List<SuccessChallengeResponse> responses = challengeQueryService.searchSuccessOfMine(
                    tokenPayload, PageRequest.of(2, 3));

            // then
            assertThat(responses).isEmpty();
        }
    }

    @DisplayName("모든 챌린지를 참여 중인 사람 수 기준 내림차순으로")
    @Nested
    class FindAllWithChallengerCountSortTest {

        @BeforeEach
        void setUp() {
            // given
            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now); // 진행 중
            fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(3L)); // 실패
            fixture.사이클_생성(토닉_ID, 미라클_모닝_ID, Progress.SUCCESS, now.minusDays(3L)); // 성공
            fixture.사이클_생성(더즈_ID, 스모디_방문하기_ID, Progress.FIRST, now.minusDays(1L)); // 진행 중
            fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.NOTHING, now); // 진행 중
            fixture.사이클_생성(토닉_ID, 스모디_방문하기_ID, Progress.SUCCESS, now.minusDays(3L)); //성공
        }

        @DisplayName("정렬")
        @Test
        void findAllWithChallengerCount_sort() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(0, 10), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, 알고리즘_풀기_ID, JPA_공부_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(2, 1, 0, 0, 0)
            );
        }

        @DisplayName("정렬 후 0페이지의 2개만 조회")
        @Test
        void findAllWithChallengerCount_pageFullSize() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(0, 2), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(2, 1)
            );
        }

        @DisplayName("정렬 후 1페이지의 1개만 조회")
        @Test
        void findAllWithChallengerCount_pagePartialSize() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(1, 2), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(오늘의_운동_ID, 알고리즘_풀기_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(0, 0)
            );
        }

        @DisplayName("정렬 후 최대 페이지를 초과한 페이지 조회")
        @Test
        void findAllWithChallengerCount_pageOverMaxPage() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(3, 2), null);

            // then
            assertThat(challengeResponses).isEmpty();
        }

        @DisplayName("정렬하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_sortAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(0, 10), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, 알고리즘_풀기_ID, JPA_공부_ID),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getIsInProgress))
                            .containsExactly(true, true, false, false, false)
            );
        }

        @DisplayName("정렬 후 0페이지의 2개만 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pageFullSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(0, 2), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(2, 1),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getIsInProgress))
                            .containsExactly(true, true)
            );
        }

        @DisplayName("정렬 후 1페이지의 1개만 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pagePartialSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(1, 2), null);

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(0, 0),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(오늘의_운동_ID, 알고리즘_풀기_ID),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getIsInProgress))
                            .containsExactly(false, false)
            );
        }

        @DisplayName("정렬 후 최대 페이지를 초과한 페이지 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pageOverMaxPageAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(3, 2), null);

            // then
            assertThat(challengeResponses).isEmpty();
        }
    }

    @DisplayName("비회원이 하나의 챌린지를 상세 조회")
    @Test
    void findOneWithChallengerCount() {
        // given
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
        fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
        fixture.사이클_생성(토닉_ID, 미라클_모닝_ID, Progress.SUCCESS, now.minusDays(3L));

        // when
        ChallengeResponse challengeResponse = challengeQueryService.findOneWithChallengerCount(now, 미라클_모닝_ID);

        // then
        assertAll(
                () -> assertThat(challengeResponse.getChallengerCount()).isEqualTo(2),
                () -> assertThat(challengeResponse.getChallengeId()).isEqualTo(미라클_모닝_ID),
                () -> assertThat(challengeResponse.getIsInProgress()).isFalse()
        );
    }

    @DisplayName("회원이 하나의 챌린지를 상세 조회")
    @Test
    void findOneWithChallengerCount_auth() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
        fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
        fixture.사이클_생성(토닉_ID, 미라클_모닝_ID, Progress.SUCCESS, now.minusDays(3L));

        // when
        ChallengeResponse challengeResponse = challengeQueryService
                .findOneWithChallengerCount(tokenPayload, now, 미라클_모닝_ID);

        // then
        assertAll(
                () -> assertThat(challengeResponse.getChallengerCount()).isEqualTo(2),
                () -> assertThat(challengeResponse.getChallengeId()).isEqualTo(미라클_모닝_ID),
                () -> assertThat(challengeResponse.getIsInProgress()).isTrue()
        );
    }

    @DisplayName("챌린지의 참여자를 조회")
    @Test
    void findAllChallenger() {
        // given
        Member member1 = fixture.회원_조회(조조그린_ID);
        Member member2 = fixture.회원_조회(더즈_ID);
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
        fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
        fixture.사이클_생성(토닉_ID, 오늘의_운동_ID, Progress.SUCCESS, now.minusDays(1L));

        // when
        List<ChallengersResponse> challengersResponse = challengeQueryService
                .findAllChallengers(미라클_모닝_ID);

        // then
        assertAll(
                () -> assertThat(challengersResponse.size()).isEqualTo(2),
                () -> assertThat(challengersResponse.stream().map(ChallengersResponse::getMemberId))
                        .containsExactly(member1.getId(), member2.getId()),
                () -> assertThat(challengersResponse.stream().map(ChallengersResponse::getNickName))
                        .containsExactly(member1.getNickname(), member2.getNickname()),
                () -> assertThat(challengersResponse.stream().map(ChallengersResponse::getProgressCount))
                        .containsExactly(0, 1),
                () -> assertThat(challengersResponse.stream().map(ChallengersResponse::getPicture))
                        .containsExactly(member1.getPicture(), member2.getPicture()),
                () -> assertThat(challengersResponse.stream().map(ChallengersResponse::getIntroduction))
                        .containsExactly(member1.getIntroduction(), member2.getIntroduction())
        );
    }

    @DisplayName("아무도 진행하지 않는 챌린지의 참여자를 조회할 때")
    @Test
    void findAllChallenger_noInprogress() {
        // given
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
        fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
        fixture.사이클_생성(토닉_ID, 오늘의_운동_ID, Progress.SUCCESS, now.minusDays(1L));

        // when
        List<ChallengersResponse> challengersResponse = challengeQueryService
                .findAllChallengers(알고리즘_풀기_ID);

        // then
        assertThat(challengersResponse).isEmpty();
    }

    @DisplayName("존재하지 않는 챌린지의 참여자를 조회할 때")
    @Test
    void findAllChallenger_notExists() {
        // given
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now);
        fixture.사이클_생성(더즈_ID, 미라클_모닝_ID, Progress.FIRST, now.minusDays(1L));
        fixture.사이클_생성(토닉_ID, 오늘의_운동_ID, Progress.SUCCESS, now.minusDays(1L));

        // when then
        assertThatThrownBy(() -> challengeQueryService.findAllChallengers(100L))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CHALLENGE);
    }

    @DisplayName("챌린지를 생성하는 경우")
    @Test
    void create() {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                "1일 1포스팅 챌린지", "1일 1포스팅하는 챌린지입니다", 0, 1);

        // when
        Long challengeId = challengeService.create(challengeRequest);

        // when then
        assertThat(challengeService.search(challengeId)).isNotNull();
    }

    @DisplayName("중복된 챌린지 이름으로 생성하는 경우 예외 발생")
    @Test
    void create_duplicatedName() {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                "1일 1포스팅 챌린지", "1일 1포스팅하는 챌린지입니다", 0, 1);

        // when
        Long challengeId = challengeService.create(challengeRequest);

        // when then
        assertThat(challengeId).isNotNull();
    }

    @DisplayName("챌린지 소개 내용이 공백문자거나 빈 문자인 경우 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"   ", ""})
    void create_emptyDesc(String invalidDescription) {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                "1일 1포스팅 챌린지", invalidDescription, 0, 1);

        // when then
        assertThatThrownBy(() -> challengeService.create(challengeRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_CHALLENGE_DESCRIPTION);
    }

    @DisplayName("챌린지 소개 길이가 255자 초과인 경우 예외 발생")
    @Test
    void create_overDescriptionSize() {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                "1일 1포스팅 챌린지", "a".repeat(256), 0, 1);

        // when then
        assertThatThrownBy(() -> challengeService.create(challengeRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_CHALLENGE_DESCRIPTION);
    }

    @DisplayName("챌린지 이름 내용이 공백문자거나 빈 문자인 경우 예외 발생")
    @ParameterizedTest
    @ValueSource(strings = {"   ", ""})
    void create_emptyName(String invalidName) {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                invalidName, "1일 1포스팅 챌린지입니다", 0, 1);

        // when then
        assertThatThrownBy(() -> challengeService.create(challengeRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_CHALLENGE_NAME);
    }

    @DisplayName("챌린지 이름 길이가 30자 초과인 경우 예외 발생")
    @Test
    void create_overNameSize() {
        // given
        ChallengeRequest challengeRequest = new ChallengeRequest(
                "a".repeat(31), "1일 1포스팅 챌린지입니다", 0, 1);

        // when then
        assertThatThrownBy(() -> challengeService.create(challengeRequest))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_CHALLENGE_NAME);
    }

    @DisplayName("비회원이 챌린지를 이름 기준으로 검색하는 경우")
    @Test
    void searchByName_unAuthorized() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                now, PageRequest.of(0, 10), "기");

        // then
        assertAll(
                () -> assertThat(challengeTabResponse.size()).isEqualTo(2),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengeId))
                        .containsExactly(스모디_방문하기_ID, 알고리즘_풀기_ID),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengerCount))
                        .containsExactly(2, 1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getIsInProgress))
                        .containsExactly(false, false)
        );
    }

    @DisplayName("비회원이 챌린지를 빈 이름 혹은 공백 문자로 검색하는 경우")
    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    void searchByName_unAuthorizedWithNoName(String invalidSearchingName) {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);

        // when then
        assertThatThrownBy(() -> challengeQueryService.findAllWithChallengerCount(
                now, PageRequest.of(0, 10), invalidSearchingName))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_SEARCH_NAME);
    }

    @DisplayName("회원이 챌린지를 이름 기준으로 검색하는 경우")
    @Test
    void searchByName_authorized() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);
        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                tokenPayload, now, PageRequest.of(0, 10), "기");

        // then
        assertAll(
                () -> assertThat(challengeTabResponse.size()).isEqualTo(2),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengeId))
                        .containsExactly(스모디_방문하기_ID, 알고리즘_풀기_ID),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengerCount))
                        .containsExactly(2, 1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getIsInProgress))
                        .containsExactly(false, true)
        );
    }

    @DisplayName("회원이 챌린지를 빈 이름 혹은 공백 문자로 검색하는 경우")
    @ParameterizedTest
    @ValueSource(strings = {" ", ""})
    void searchByName_authorizedWithNoName(String invalidSearchingName) {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);
        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when then
        assertThatThrownBy(() -> challengeQueryService.findAllWithChallengerCount(
                tokenPayload, now, PageRequest.of(0, 10), invalidSearchingName))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_SEARCH_NAME);
    }

    @DisplayName("회원이 참가한 챌린지 하나를 조회하는 경우")
    @Test
    void findOneWithMine() {
        //given
        fixture.사이클_생성_FIRST(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_SUCCESS(알파_ID, 알고리즘_풀기_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(알파_ID, 알고리즘_풀기_ID, now.minusDays(6L));
        fixture.사이클_생성_SECOND(알파_ID, 알고리즘_풀기_ID, now.minusDays(9L));

        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when
        ChallengeHistoryResponse challengeHistoryResponse = challengeQueryService.findOneWithMine(
                tokenPayload, 알고리즘_풀기_ID);

        // then
        assertAll(
                () -> assertThat(challengeHistoryResponse.getChallengeName()).isEqualTo("알고리즘 풀기"),
                () -> assertThat(challengeHistoryResponse.getSuccessCount()).isEqualTo(2),
                () -> assertThat(challengeHistoryResponse.getCycleDetailCount()).isEqualTo(8)
        );
    }

    @DisplayName("회원이 참가한 챌린지 하나를 조회하는 경우")
    @Test
    void findOneWithMine_notParticipate() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_SUCCESS(알파_ID, 알고리즘_풀기_ID, now.minusDays(3L));
        fixture.사이클_생성_SUCCESS(알파_ID, 알고리즘_풀기_ID, now.minusDays(6L));
        fixture.사이클_생성_SECOND(알파_ID, 알고리즘_풀기_ID, now.minusDays(9L));

        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when then
        assertThatThrownBy(() -> challengeQueryService.findOneWithMine(tokenPayload, 오늘의_운동_ID))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CHALLENGE);
    }
}
