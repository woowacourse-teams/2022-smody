package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeQueryService challengeQueryService;

    @Autowired
    private ResourceFixture fixture;

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
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(0, 10));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                            .containsExactly(2, 1, 0, 0, 0)
            );
        }

        @DisplayName("정렬 후 0페이지의 2개만 조회")
        @Test
        void findAllWithChallengerCount_pageFullSize() {
            // when
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(0, 2));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                            .containsExactly(2, 1)
            );
        }

        @DisplayName("정렬 후 1페이지의 1개만 조회")
        @Test
        void findAllWithChallengerCount_pagePartialSize() {
            // when
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(1, 2));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                            .containsExactly(0, 0)
            );
        }

        @DisplayName("정렬 후 최대 페이지를 초과한 페이지 조회")
        @Test
        void findAllWithChallengerCount_pageOverMaxPage() {
            // when
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    now, PageRequest.of(3, 2));

            // then
            assertThat(challengeResponses).isEmpty();
        }

        @DisplayName("정렬하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_sortAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(0, 10));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(5),
                    () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getIsInProgress))
                            .containsExactly(true, true, false, false, false)
            );
        }

        @DisplayName("정렬 후 0페이지의 2개만 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pageFullSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(0, 2));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                            .containsExactly(2, 1),
                    () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getIsInProgress))
                            .containsExactly(true, true)
            );
        }

        @DisplayName("정렬 후 1페이지의 1개만 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pagePartialSizeAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(1, 2));

            // then
            assertAll(
                    () -> assertThat(challengeResponses.size()).isEqualTo(2),
                    () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                            .containsExactly(0, 0),
                    () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getIsInProgress))
                            .containsExactly(false, false)
            );
        }

        @DisplayName("정렬 후 최대 페이지를 초과한 페이지 조회하면서 회원인 경우")
        @Test
        void findAllWithChallengerCount_pageOverMaxPageAuth() {
            // when
            TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
            List<ChallengeResponse> challengeResponses = challengeQueryService.findAllWithChallengerCount(
                    tokenPayload, now, PageRequest.of(3, 2));

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
}
