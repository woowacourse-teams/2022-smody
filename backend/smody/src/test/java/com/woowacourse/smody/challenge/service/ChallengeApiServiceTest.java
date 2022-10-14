package com.woowacourse.smody.challenge.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.challenge.dto.ChallengeHistoryResponse;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
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

        System.out.println("#####");
        // when
        ChallengeHistoryResponse challengeHistoryResponse =
                challengeApiService.findByMeAndChallenge(new TokenPayload(조조그린_ID), 미라클_모닝_ID);
        System.out.println("#####");

        // then
        assertAll(
                () -> assertThat(challengeHistoryResponse.getChallengeName()).isEqualTo("미라클 모닝"),
                () -> assertThat(challengeHistoryResponse.getSuccessCount()).isEqualTo(1),
                () -> assertThat(challengeHistoryResponse.getCycleDetailCount()).isEqualTo(6)
        );
    }
}
