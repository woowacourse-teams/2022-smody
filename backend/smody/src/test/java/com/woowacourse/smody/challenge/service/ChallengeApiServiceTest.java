package com.woowacourse.smody.challenge.service;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
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
                new PagingParams("popular", 3));

        // then
        assertThat(actual).map(ChallengeTabResponse::getChallengeName)
                .containsExactly("미라클 모닝", "오늘의 운동", "스모디 방문하기");
    }
}
