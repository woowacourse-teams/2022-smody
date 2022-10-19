package com.woowacourse.smody.challenge.service;

import static com.woowacourse.smody.support.ResourceFixture.JPA_공부_ID;
import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알고리즘_풀기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;

class ChallengeServiceTest extends IntegrationTest {

    @Autowired
    private ChallengeService challengeService;

    private final LocalDateTime now = LocalDateTime.now();

    @DisplayName("id로 챌린지를 조회할 때")
    @Nested
    class Search {

        @DisplayName("성공")
        @Test
        void success() {
            // when
            Challenge actual = challengeService.search(스모디_방문하기_ID);

            // then
            assertThat(actual.getName()).isEqualTo("스모디 방문하기");
        }

        @DisplayName("없는 경우 예외를 발생시킨다.")
        @Test
        void notFound_exception() {
            // then
            assertThatThrownBy(() -> challengeService.search(0L))
                    .isInstanceOf(BusinessException.class)
                    .extracting("ExceptionData")
                    .isEqualTo(ExceptionData.NOT_FOUND_CHALLENGE);
        }
    }

    @DisplayName("id로 챌린지를 조회할 때")
    @Nested
    class FindById {

        @DisplayName("찾으면 Optional에 담아서 반환한다.")
        @Test
        void success() {
            // when
            Optional<Challenge> actual = challengeService.findById(스모디_방문하기_ID);

            // then
            assertThat(actual).isPresent();
        }

        @DisplayName("못찾으면 Option.empty()를 반환한다.")
        @Test
        void notFound() {
            // when
            Optional<Challenge> actual = challengeService.findById(0L);

            // then
            assertThat(actual).isEmpty();
        }
    }

    @DisplayName("챌린지를 조회할 때")
    @Nested
    class FindAllByFilter {

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
            List<Challenge> challenges = challengeService.findAllByFilter(new PagingParams(null, null, 0L, null));

            // then
            assertAll(
                    () -> assertThat(challenges).hasSize(5),
                    () -> assertThat(challenges.stream().mapToLong(Challenge::getId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID, 오늘의_운동_ID, 알고리즘_풀기_ID, JPA_공부_ID)
            );
        }

        @DisplayName("2개만 조회")
        @Test
        void findAllWithChallengerCount_pageFullSize() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(
                    new PagingParams(null, 2, 0L, null));

            // then
            assertAll(
                    () -> assertThat(challenges).hasSize(2),
                    () -> assertThat(challenges.stream().map(Challenge::getId))
                            .containsExactly(스모디_방문하기_ID, 미라클_모닝_ID)
            );
        }

        @DisplayName("커서 기준 2개만 조회")
        @Test
        void findAllWithChallengerCount_pagePartialSize() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(
                    new PagingParams(null, 2, 미라클_모닝_ID, null)
            );

            // then
            assertAll(
                    () -> assertThat(challenges).hasSize(2),
                    () -> assertThat(challenges.stream().mapToLong(Challenge::getId))
                            .containsExactly(오늘의_운동_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("데이터의 마지막이 커서이면서 3개 조회")
        @Test
        void findAllWithChallengerCount_pageOverMaxPage() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(
                    new PagingParams(null, 3, JPA_공부_ID, null));

            // then
            assertThat(challenges).isEmpty();
        }

        @DisplayName("빈 이름 혹은 공백 문자로 검색하는 경우 예외를 발생시킨다.")
        @ParameterizedTest
        @ValueSource(strings = {" ", ""})
        void searchByName_unAuthorizedWithNoName(String invalidSearchingName) {
            // when then
            assertThatThrownBy(() -> challengeService.findAllByFilter(
                    new PagingParams(null, null, 0L, invalidSearchingName)))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.INVALID_SEARCH_NAME);
        }

        @DisplayName("이름 기준으로 검색하는 경우")
        @Test
        void searchByName_authorized() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(new PagingParams(null, null, 0L, "기"));

            // then
            assertAll(
                    () -> assertThat(challenges).hasSize(2),
                    () -> assertThat(challenges.stream().map(Challenge::getId))
                            .containsExactly(스모디_방문하기_ID, 알고리즘_풀기_ID)
            );
        }

        @DisplayName("이름 기준으로 검색 후 페이지네이션")
        @Test
        void searchByName_authorizedCursorPaging() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(
                    new PagingParams(null, null, 스모디_방문하기_ID, "기"));

            // then
            assertAll(
                    () -> assertThat(challenges).hasSize(1),
                    () -> assertThat(challenges.stream().map(Challenge::getId))
                            .containsExactly(알고리즘_풀기_ID)
            );
        }

        @DisplayName("이름 기준으로 검색하고 마지막 커서로 페이지네이션")
        @Test
        void searchByName_authorizedLastCursor() {
            // when
            List<Challenge> challenges = challengeService.findAllByFilter(
                    new PagingParams(null, null, 알고리즘_풀기_ID, "기"));

            // then
            assertThat(challenges).isEmpty();
        }
    }

    @DisplayName("챌린지를 생성할 때")
    @Nested
    class Create {

        @DisplayName("성공")
        @Test
        void create() {
            // when
            Long challengeId = challengeService.create(
                    "1일 1포스팅 챌린지", "1일 1포스팅하는 챌린지입니다", 0, 1
            );

            // when then
            assertThat(challengeService.search(challengeId)).isNotNull();
        }

        @DisplayName("중복된 챌린지 이름으로 생성하는 경우 예외 발생")
        @Test
        void create_duplicatedName() {
            // when
            Long challengeId = challengeService.create(
                    "1일 1포스팅 챌린지", "1일 1포스팅하는 챌린지입니다", 0, 1
            );

            // when then
            assertThat(challengeId).isNotNull();
        }

        @DisplayName("챌린지 소개 내용이 공백문자거나 빈 문자인 경우 예외 발생")
        @ParameterizedTest
        @ValueSource(strings = {"   ", ""})
        void create_emptyDesc(String invalidDescription) {
            // when then
            assertThatThrownBy(() -> challengeService.create("1일 1포스팅 챌린지", invalidDescription, 0, 1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.INVALID_CHALLENGE_DESCRIPTION);
        }

        @DisplayName("챌린지 소개 길이가 255자 초과인 경우 예외 발생")
        @Test
        void create_overDescriptionSize() {
            // when then
            assertThatThrownBy(() -> challengeService.create("1일 1포스팅 챌린지", "a".repeat(256), 0, 1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.INVALID_CHALLENGE_DESCRIPTION);
        }

        @DisplayName("챌린지 이름 내용이 공백문자거나 빈 문자인 경우 예외 발생")
        @ParameterizedTest
        @ValueSource(strings = {"   ", ""})
        void create_emptyName(String invalidName) {
            // when then
            assertThatThrownBy(() -> challengeService.create(invalidName, "1일 1포스팅 챌린지입니다", 0, 1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.INVALID_CHALLENGE_NAME);
        }

        @DisplayName("챌린지 이름 길이가 30자 초과인 경우 예외 발생")
        @Test
        void create_overNameSize() {
            // when then
            assertThatThrownBy(() -> challengeService.create("a".repeat(31), "1일 1포스팅 챌린지입니다", 0, 1))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.INVALID_CHALLENGE_NAME);
        }
    }
}
