package com.woowacourse.smody.challenge.service;

import static com.woowacourse.smody.support.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeOfMineResponse;
import com.woowacourse.smody.challenge.dto.ChallengeRequest;
import com.woowacourse.smody.challenge.dto.ChallengeResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.challenge.dto.ChallengersResponse;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

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

            fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now.minusSeconds(1L));
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
        void searchOfMine() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, null, 0L, null));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(5),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(0, 2, 1, 3, 1),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, JPA_공부_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("0페이지의 3개만 조회")
        @Test
        void searchOfMine_pageFullSize() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 3, 0L, null));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(3),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(0, 2, 1),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID)
            );
        }

        @DisplayName("1페이지의 2개만 조회")
        @Test
        void searchOfMine_pagePartialSize() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 2, 미라클_모닝_ID, null));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(2),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(1, 3),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(오늘의_운동_ID, JPA_공부_ID)
            );
        }

        @DisplayName("없는 페이지로 조회")
        @Test
        void searchOfMine_pageOverMaxPage() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 3, 알고리즘_풀기_ID, null));

            // then
            assertThat(responses).isEmpty();
        }

        @DisplayName("성공한 챌린지들만 조회")
        @Test
        void searchSuccessOfMine() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, null, 0L, "success"));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(4),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(2, 1, 3, 1),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(미라클_모닝_ID, 오늘의_운동_ID, JPA_공부_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("내가 성공한 챌린지들의 0페이지의 3개만 조회")
        @Test
        void searchSuccessOfMine_pageFullSize() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 3, 0L, "success"));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(3),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(2, 1, 3),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(미라클_모닝_ID, 오늘의_운동_ID, JPA_공부_ID)
            );
        }

        @DisplayName("내가 성공한 챌린지들의 1페이지의 2개만 조회")
        @Test
        void searchSuccessOfMine_pagePartialSize() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 2, 오늘의_운동_ID, "success"));

            // then
            assertAll(
                    () -> assertThat(responses.size()).isEqualTo(2),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getSuccessCount)
                            .containsExactly(3, 1),
                    () -> assertThat(responses)
                            .map(ChallengeOfMineResponse::getChallengeId)
                            .containsExactly(JPA_공부_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("내가 성공한 챌린지들의 없는 페이지로 조회")
        @Test
        void searchSuccessOfMine_pageOverMaxPage() {
            // when
            List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                    tokenPayload, new PagingParams(null, 3, 알고리즘_풀기_ID, "success"));

            // then
            assertThat(responses).isEmpty();
        }
    }

    @DisplayName("내 전체 참여 챌린지 조회에서 같은 인증 시간이라면 id순으로 정렬")
    @Test
    void searchOfMine_sameTime() {
        // given
        fixture.사이클_생성(조조그린_ID, 미라클_모닝_ID, Progress.NOTHING, now.minusDays(2L));
        fixture.사이클_생성(조조그린_ID, 오늘의_운동_ID, Progress.NOTHING, now.minusDays(2L));
        fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.SECOND, now.minusDays(2L));
        fixture.사이클_생성(조조그린_ID, 스모디_방문하기_ID, Progress.SUCCESS, now.minusDays(7L));
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);

        List<ChallengeOfMineResponse> responses = challengeQueryService.searchOfMineWithFilter(
                tokenPayload, new PagingParams(null, null, 0L, null));

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(3),
                () -> assertThat(responses)
                        .map(ChallengeOfMineResponse::getSuccessCount)
                        .containsExactly(1, 0, 0),
                () -> assertThat(responses)
                        .map(ChallengeOfMineResponse::getChallengeId)
                        .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID)
        );

    }

    @DisplayName("모든 챌린지를 ")
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

        @DisplayName("조회")
        @Test
        void findAllWithChallengerCount_sort() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, new PagingParams(null, null, 0L, null));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, 알고리즘_풀기_ID, JPA_공부_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(2, 1, 0, 0, 0)
            );
        }

        @DisplayName("2개만 조회")
        @Test
        void findAllWithChallengerCount_pageFullSize() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(now,
                    new PagingParams(null, 2, 0L, null));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(2, 1)
            );
        }

        @DisplayName("커서 기준 2개만 조회")
        @Test
        void findAllWithChallengerCount_pagePartialSize() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, new PagingParams(null, 2, 미라클_모닝_ID, null));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(오늘의_운동_ID, 알고리즘_풀기_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeTabResponse::getChallengerCount))
                            .containsExactly(0, 0)
            );
        }

        @DisplayName("데이터의 마지막이 커서이면서 3개 조회")
        @Test
        void findAllWithChallengerCount_pageOverMaxPage() {
            // when
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, new PagingParams(null, 3, JPA_공부_ID, null));

            // then
            assertThat(challengeResponses).isEmpty();
        }

        @DisplayName("회원이 조회")
        @Test
        void findAllWithChallengerCount_sortAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, new PagingParams(null, null, 0L, null));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().mapToLong(ChallengeTabResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, 알고리즘_풀기_ID, JPA_공부_ID),
                    () -> assertThat(challengeResponses.stream().map(ChallengeTabResponse::getIsInProgress))
                            .containsExactly(true, true, false, false, false)
            );
        }

        @DisplayName("회원이 2개 조회")
        @Test
        void findAllWithChallengerCount_pageFullSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, new PagingParams(null, 2, 0L, null));

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

        @DisplayName("회원이 커서를 기준으로 2개를 조회")
        @Test
        void findAllWithChallengerCount_pagePartialSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, new PagingParams(null, 2, 미라클_모닝_ID, null));

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

        @DisplayName("회원이 데이터의 마지막이 커서이면서 3개 조회")
        @Test
        void findAllWithChallengerCount_pageOverMaxPageAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeTabResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, new PagingParams(null, 3, JPA_공부_ID, null));

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
        ChallengeResponse challengeResponse = challengeQueryService.findWithChallengerCount(now, 미라클_모닝_ID);

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
                .findWithChallengerCount(tokenPayload, now, 미라클_모닝_ID);

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
                now, new PagingParams(null, null, 0L, "기"));

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

    @DisplayName("비회원이 챌린지를 이름 기준으로 검색 후 페이지네이션")
    @Test
    void searchByName_unAuthorizedCursorPaging() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                now, new PagingParams(null, null, 스모디_방문하기_ID, "기"));

        // then
        assertAll(
                () -> assertThat(challengeTabResponse.size()).isEqualTo(1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengeId))
                        .containsExactly(알고리즘_풀기_ID),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengerCount))
                        .containsExactly( 1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getIsInProgress))
                        .containsExactly(false)
        );
    }

    @DisplayName("비회원이 챌린지를 이름 기준으로 검색하고 마지막 커서로 페이지네이션")
    @Test
    void searchByName_unAuthorizedLastCursor() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                now, new PagingParams(null, null, 알고리즘_풀기_ID, "기"));

        // then
        assertThat(challengeTabResponse).isEmpty();
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
                now, new PagingParams(null, null, 0L, invalidSearchingName)))
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
                tokenPayload, now, new PagingParams(null, null, 0L, "기"));

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

    @DisplayName("회원이 챌린지를 이름 기준으로 검색 후 페이지네이션")
    @Test
    void searchByName_authorizedCursorPaging() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);
        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                tokenPayload, now, new PagingParams(null, null, 스모디_방문하기_ID, "기"));

        // then
        assertAll(
                () -> assertThat(challengeTabResponse.size()).isEqualTo(1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengeId))
                        .containsExactly(알고리즘_풀기_ID),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getChallengerCount))
                        .containsExactly( 1),
                () -> assertThat(challengeTabResponse.stream().map(ChallengeTabResponse::getIsInProgress))
                        .containsExactly(true)
        );
    }

    @DisplayName("회원이 챌린지를 이름 기준으로 검색하고 마지막 커서로 페이지네이션")
    @Test
    void searchByName_authorizedLastCursor() {
        //given
        fixture.사이클_생성_NOTHING(알파_ID, 알고리즘_풀기_ID, now);
        fixture.사이클_생성_NOTHING(토닉_ID, 스모디_방문하기_ID, now);
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, now);
        TokenPayload tokenPayload = new TokenPayload(알파_ID);

        // when
        List<ChallengeTabResponse> challengeTabResponse = challengeQueryService.findAllWithChallengerCount(
                tokenPayload, now, new PagingParams(null, null, 알고리즘_풀기_ID, "기"));

        // then
        assertThat(challengeTabResponse).isEmpty();
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
                tokenPayload, now, new PagingParams(null, null, 0L, invalidSearchingName)))
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
        ChallengeHistoryResponse challengeHistoryResponse = challengeQueryService.findWithMine(
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

        // when
        ChallengeHistoryResponse challengeHistoryResponse = challengeQueryService.findWithMine(tokenPayload, 오늘의_운동_ID);

        // then
        assertAll(
                () -> assertThat(challengeHistoryResponse.getChallengeName()).isEqualTo("오늘의 운동"),
                () -> assertThat(challengeHistoryResponse.getSuccessCount()).isEqualTo(0),
                () -> assertThat(challengeHistoryResponse.getCycleDetailCount()).isEqualTo(0)
        );
    }
}
