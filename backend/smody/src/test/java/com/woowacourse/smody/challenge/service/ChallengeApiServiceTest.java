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
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.challenge.dto.ChallengeTabResponse;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChallengeApiServiceTest extends IntegrationTest {

    @Autowired
    private ChallengeApiService challengeApiService;

    @DisplayName("나의 특정 챌린지에 대한 사이클을 성공 횟수, 인증 횟수와 함께 조회")
    @Test
    void findByMeAndChallenge() {
        // given
        fixture.사이클_생성_SUCCESS(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());

        // when
        ChallengeHistoryResponse challengeHistoryResponse =
                challengeApiService.findByMeAndChallenge(new TokenPayload(조조그린_ID), 미라클_모닝_ID);

        // then
        assertAll(
                () -> assertThat(challengeHistoryResponse.getChallengeName()).isEqualTo("미라클 모닝"),
                () -> assertThat(challengeHistoryResponse.getSuccessCount()).isEqualTo(1),
                () -> assertThat(challengeHistoryResponse.getCycleDetailCount()).isEqualTo(6)
        );
    }

    @DisplayName("참가자 순으로 정렬하여 조회")
    @Test
    void findAllWithChallengerCountByFilter_popular() {
        // given
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(알파_ID, 미라클_모닝_ID, LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 오늘의_운동_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 오늘의_운동_ID, LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, LocalDateTime.now());

        // when
        List<ChallengeTabResponse> actual = challengeApiService.findAllWithChallengerCountByFilter(LocalDateTime.now(),
                new PagingParams("popular", 10));

        // then
        assertThat(actual).map(ChallengeTabResponse::getChallengeName)
                .containsExactly("미라클 모닝", "오늘의 운동", "스모디 방문하기");
    }

    @DisplayName("참가자 순으로 size에 맞게 조회")
    @Test
    void findAllWithChallengerCountByFilter_popular_size() {
        // given
        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 미라클_모닝_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(알파_ID, 미라클_모닝_ID, LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 오늘의_운동_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 오늘의_운동_ID, LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기_ID, LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기_ID, LocalDateTime.now());

        // when
        List<ChallengeTabResponse> actual = challengeApiService.findAllWithChallengerCountByFilter(LocalDateTime.now(),
                new PagingParams("popular", 2));

        // then
        assertThat(actual).map(ChallengeTabResponse::getChallengeName)
                .containsExactly("미라클 모닝", "오늘의 운동");
    }

    @DisplayName("참가자 순으로 조회할 때 참가자가 없는 챌린지는 조회하지 않는다.")
    @Test
    void findAllWithChallengerCountByFilter_popular_notExistEmptyChallengers() {
        // given
        Challenge 미라클_모닝 = fixture.챌린지_조회(미라클_모닝_ID);
        Challenge 오늘의_운동 = fixture.챌린지_조회(오늘의_운동_ID);
        Challenge 스모디_방문하기 = fixture.챌린지_조회(스모디_방문하기_ID);
        Challenge 알고리즘_풀기 = fixture.챌린지_조회(알고리즘_풀기_ID);
        Challenge JPA_공부 = fixture.챌린지_조회(JPA_공부_ID);

        fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 미라클_모닝.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 미라클_모닝.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(알파_ID, 미라클_모닝.getId(), LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 오늘의_운동.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(토닉_ID, 오늘의_운동.getId(), LocalDateTime.now());

        fixture.사이클_생성_NOTHING(조조그린_ID, 스모디_방문하기.getId(), LocalDateTime.now());
        fixture.사이클_생성_NOTHING(더즈_ID, 스모디_방문하기.getId(), LocalDateTime.now());

        // when
        List<ChallengeTabResponse> actual = challengeApiService.findAllWithChallengerCountByFilter(LocalDateTime.now(),
                new PagingParams("popular", 5));

        // then
        assertThat(actual).map(ChallengeTabResponse::getChallengeName)
                .containsExactly("미라클 모닝", "오늘의 운동", "스모디 방문하기");
    }
}
