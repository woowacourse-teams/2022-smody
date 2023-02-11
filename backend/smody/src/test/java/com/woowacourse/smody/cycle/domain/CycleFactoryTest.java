package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@SuppressWarnings("NonAsciiCharacters")
@ExtendWith(MockitoExtension.class)
class CycleFactoryTest {

    @Mock
    private Member member;

    @Mock
    private Challenge challenge;

    @Mock
    private CycleRepository cycleRepository;

    private CycleFactory cycleFactory;

    @BeforeEach
    void init() {
        given(member.getId()).willReturn(조조그린_ID);
        given(challenge.getId()).willReturn(미라클_모닝_ID);
        cycleFactory = new CycleFactory(cycleRepository);
    }

    @DisplayName("사이클을 생성한다.")
    @Test
    void create() {
        // given
        given(cycleRepository.findRecent(조조그린_ID, 미라클_모닝_ID))
                .willReturn(Optional.empty());

        LocalDateTime startTime = LocalDateTime.now();

        // when
        Cycle actual = cycleFactory.create(member, challenge, startTime);

        // then
        assertThat(actual.getStartTime()).isEqualTo(startTime);
    }

    @DisplayName("동일한 첼린지에 진행중인 사이클이 존재하는 경우 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_duplicateInProgressChallenge() {
        // given
        given(cycleRepository.findRecent(조조그린_ID, 미라클_모닝_ID))
                .willReturn(
                        Optional.of(new Cycle(member, challenge, Progress.FIRST, LocalDateTime.now().minusDays(1)))
                );

        LocalDateTime startTime = LocalDateTime.now();

        // when then
        assertThatThrownBy(() -> cycleFactory.create(member, challenge, startTime))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);
    }

    @DisplayName("현재 시각 기준 24시간이 지난 시작시간을 가진 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_overOneDay() {
        // given
        given(cycleRepository.findRecent(조조그린_ID, 미라클_모닝_ID))
                .willReturn(Optional.empty());

        LocalDateTime startTime = LocalDateTime.now()
                .plusDays(1)
                .plusSeconds(1);

        // when then
        assertThatThrownBy(() -> cycleFactory.create(member, challenge, startTime))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_START_TIME);
    }

    @DisplayName("오늘 성공한 챌린지로 다시 사이클을 생성한 경우 사이클을 내일 날짜로 생성한다.")
    @Test
    void create_alreadySuccessChallenge() {
        // given
        LocalDateTime beforeStartTime = LocalDateTime.now().minusDays(2);
        given(cycleRepository.findRecent(조조그린_ID, 미라클_모닝_ID))
                .willReturn(
                        Optional.of(new Cycle(member, challenge, Progress.SUCCESS, beforeStartTime))
                );

        LocalDateTime startTime = LocalDateTime.now();

        // when
        Cycle actual = cycleFactory.create(member, challenge, startTime);

        // then
        assertThat(actual.getStartTime()).isEqualTo(beforeStartTime.plusDays(3));
    }
}
