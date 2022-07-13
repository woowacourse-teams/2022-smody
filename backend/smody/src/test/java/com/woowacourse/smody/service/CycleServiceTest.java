package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.*;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CycleServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private CycleService cycleService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("사이클을 생성한다.")
    @Test
    void create() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, NICKNAME, PASSWORD));

        // when
        LocalDateTime now = LocalDateTime.now();
        Long cycleId = cycleService.create(
                new TokenPayload(member.getId()),
                new CycleRequest(now, 1L)
        );
        CycleResponse cycleResponse = cycleService.findById(cycleId);

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(cycleId),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(1L),
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(now)
        );
    }

    @DisplayName("동일한 첼린지에 진행중인 사이클이 존재하는 경우 사이클을 생성할 때 예외를 발생시킨다.")
    @Test
    void create_duplicateInProgressChallenge() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        cycleRepository.save(new Cycle(member, challenge, Progress.NOTHING, LocalDateTime.now()));

        // when then
        assertThatThrownBy(() -> cycleService.create(
                new TokenPayload(member.getId()),
                new CycleRequest(LocalDateTime.now(), 1L)
        )).isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.DUPLICATE_IN_PROGRESS_CHALLENGE);

    }

    @DisplayName("오늘 성공한 챌린지로 다시 사이클을 생성한 경우 사이클을 내일 날짜로 생성한다.")
    @Test
    void create_alreadySuccessChallenge() {
        // given
        LocalDateTime now = LocalDateTime.now();
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();

        Cycle cycle = cycleRepository.save(new Cycle(member, challenge, Progress.SUCCESS, now.minusDays(2L)));
        cycleRepository.save(new Cycle(member, challenge, Progress.SECOND, now.minusDays(3L)));

        // when
        Long cycleId = cycleService.create(
                new TokenPayload(member.getId()),
                new CycleRequest(now, challenge.getId())
        );
        CycleResponse cycleResponse = cycleService.findById(cycleId);

        // then
        assertAll(
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(cycle.getStartTime().plusDays(3L)),
                () -> assertThat(cycleResponse.getProgressCount()).isEqualTo(0)
        );
    }

    @DisplayName("유효한 시간일때 사이클의 진행도를 증가시킨다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:00,1",
            "NOTHING,2022-01-01T23:59:59,1",
            "FIRST,2022-01-02T00:00:00,2",
            "FIRST,2022-01-02T23:59:59,2",
            "SECOND,2022-01-03T00:00:00,3",
            "SECOND,2022-01-03T23:59:59,3"
    })
    void increaseProgress(Progress progress, LocalDateTime progressTime, int expected) {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId());
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        Cycle cycle = new Cycle(member, challenge, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        cycleRepository.save(cycle);

        // when
        ProgressResponse progressResponse = cycleService.increaseProgress(tokenPayload,
                new ProgressRequest(cycle.getId(), progressTime));

        // then
        assertThat(progressResponse.getProgressCount()).isEqualTo(expected);
    }

    @DisplayName("진행도를 증가 시킬 때 유효하지 않은 시간인 경우 예외 발생")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2021-12-31T23:59:59",
            "NOTHING,2022-01-02T00:00:00",
            "FIRST,2022-01-01T23:59:59",
            "FIRST,2022-01-03T00:00:00",
            "SECOND,2022-01-02T23:59:59",
            "SECOND,2022-01-04T00:00:00"
    })
    void increaseProgress_failWithTime(Progress progress, LocalDateTime invalidTime) {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId());
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        Cycle cycle = new Cycle(member, challenge, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        cycleRepository.save(cycle);

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, new ProgressRequest(cycle.getId(), invalidTime)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("하루에 사이클의 진행을 두번 할 경우 예외가 발생한다.")
    @ParameterizedTest
    @CsvSource(value = {
            "NOTHING,2022-01-01T00:00:00,2022-01-01T23:59:59",
            "FIRST,2022-01-02T00:00:00,2022-01-02T23:59:59"
    })
    void increaseProgress_twoTimeInOneDay(Progress progress, LocalDateTime progressTime, LocalDateTime invalidTime) {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId());
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        Cycle cycle = new Cycle(member, challenge, progress,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        cycleRepository.save(cycle);
        cycleService.increaseProgress(tokenPayload, new ProgressRequest(cycle.getId(), progressTime));

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, new ProgressRequest(cycle.getId(), invalidTime)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("진행도를 증가 시킬 때 사이클을 찾지 못했을 경우 예외 발생")
    @Test
    void increaseProgress_notExistCycle() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId());

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, new ProgressRequest(1L, LocalDateTime.now())))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_CYCLE);
    }

    @DisplayName("진행도를 증가 시킬 때 권한에 맞지 않은 cycle일 경우 예외 발생")
    @Test
    void increaseProgress_unauthorized() {
        // given
        TokenPayload tokenPayload = new TokenPayload(1000L);
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING,
                LocalDateTime.of(2022, 1, 1, 0, 0));
        cycleRepository.save(cycle);

        // when then
        assertThatThrownBy(() ->
                cycleService.increaseProgress(tokenPayload, new ProgressRequest(cycle.getId(), LocalDateTime.now())))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.UNAUTHORIZED_MEMBER);
    }

    @DisplayName("진행 중인 자신의 모든 사이클을 조회")
    @Test
    void findAllInProgressOfMine() {
        // given
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId());
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.NOTHING, today);
        Cycle failed1 = new Cycle(member, challenge1, Progress.FIRST, today.minusDays(3L));
        Cycle success1 = new Cycle(member, challenge1, Progress.SUCCESS, today.minusDays(3L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, today);
        Cycle failed2 = new Cycle(member, challenge2, Progress.SECOND, today.minusDays(3L));
        Cycle success2 = new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(3L));
        Cycle success3 = new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(6L));
        Cycle future = new Cycle(member, challenge1, Progress.NOTHING, today.plusSeconds(1L));
        cycleRepository.saveAll(List.of(inProgress1, inProgress2, failed1, failed2, success1, success2, success3, future));

        // when
        List<CycleResponse> actual = cycleService.findAllInProgressOfMine(
                tokenPayload, today, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(actual)
                        .map(CycleResponse::getCycleId)
                        .containsAll(List.of(inProgress1.getId(), inProgress2.getId(), future.getId())),
                () -> assertThat(actual)
                        .filteredOn(response -> response.getChallengeId().equals(1L))
                        .map(CycleResponse::getSuccessCount)
                        .containsExactly(1, 1),
                () -> assertThat(actual)
                        .filteredOn(response -> response.getChallengeId().equals(2L))
                        .map(CycleResponse::getSuccessCount)
                        .containsExactly(2)
        );
    }

    @DisplayName("id로 사이클 조회 시 성공")
    @Test
    void findById() {
        // given
        Challenge challenge = challengeRepository.findById(1L).orElseThrow();
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        Cycle inProgress = new Cycle(member, challenge, Progress.NOTHING, today);
        Cycle failed1 = new Cycle(member, challenge, Progress.FIRST, today.minusDays(3L));
        Cycle failed2 = new Cycle(member, challenge, Progress.SECOND, today.minusDays(6L));
        Cycle success1 = new Cycle(member, challenge, Progress.SUCCESS, today.minusDays(9L));
        Cycle success2 = new Cycle(member, challenge, Progress.SUCCESS, today.minusDays(12L));
        cycleRepository.saveAll(List.of(inProgress, failed1, failed2, success1, success2));

        // when
        CycleResponse cycleResponse = cycleService.findById(inProgress.getId());

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(inProgress.getId()),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(inProgress.getChallenge().getId()),
                () -> assertThat(cycleResponse.getChallengeName()).isEqualTo(inProgress.getChallenge().getName()),
                () -> assertThat(cycleResponse.getProgressCount()).isEqualTo(inProgress.getProgress().getCount()),
                () -> assertThat(cycleResponse.getStartTime()).isEqualTo(inProgress.getStartTime()),
                () -> assertThat(cycleResponse.getSuccessCount()).isEqualTo(2)
        );
    }

    @DisplayName("미래 시점의 사이클은 현재 시점으로 인증 불가")
    @Test
    void progress_future_time() {
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        Challenge challenge = challengeRepository.findById(1L).get();
        LocalDateTime testTime = LocalDateTime.now();
        Cycle cycle = new Cycle(member, challenge, Progress.NOTHING, testTime.plusSeconds(1L));

        assertThatThrownBy(() -> cycle.increaseProgress(testTime))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.INVALID_PROGRESS_TIME);
    }

    @DisplayName("나의 모든 진행 중인 사이클을 남은 인증 시간 기준으로 오름차순 정렬")
    @Test
    void findAllInProgress_sort() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId(), NICKNAME);
        LocalDateTime today = LocalDateTime.now();
        Challenge challenge1 = challengeRepository.findById(1L).get();
        Challenge challenge2 = challengeRepository.findById(2L).get();
        Challenge challenge3 = challengeRepository.findById(3L).get();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge3, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge4, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge5, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge1, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge1, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        cycleRepository.saveAll(List.of(
                inProgress1, inProgress2, inProgress3, failed, success, proceed1, proceed2));

        // when
        List<CycleResponse> actual = cycleService.findAllInProgressOfMine(
                tokenPayload, today, PageRequest.of(0, 10));

        // then
        assertThat(actual)
                .map(CycleResponse::getCycleId)
                .containsExactly(inProgress1.getId(), inProgress2.getId(), inProgress3.getId(),
                        proceed2.getId(), proceed1.getId());
    }

    @DisplayName("나의 모든 진행 중인 사이클을 0페이지부터 3개만 조회")
    @Test
    void findAllInProgress_pagingFullSize() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId(), NICKNAME);
        LocalDateTime today = LocalDateTime.now();
        Challenge challenge1 = challengeRepository.findById(1L).get();
        Challenge challenge2 = challengeRepository.findById(2L).get();
        Challenge challenge3 = challengeRepository.findById(3L).get();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge3, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge4, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge5, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge1, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge1, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        cycleRepository.saveAll(List.of(
                inProgress1, inProgress2, inProgress3, failed, success, proceed1, proceed2));


        // when
        List<CycleResponse> actual = cycleService.findAllInProgressOfMine(
                tokenPayload, today, PageRequest.of(0, 3));

        // then
        assertThat(actual)
                .map(CycleResponse::getCycleId)
                .containsExactly(inProgress1.getId(), inProgress2.getId(), inProgress3.getId());
    }

    @DisplayName("나의 모든 진행 중인 사이클을 1페이지부터 2개만 조회")
    @Test
    void findAllInProgress_pagingPartialSize() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId(), NICKNAME);
        LocalDateTime today = LocalDateTime.now();
        Challenge challenge1 = challengeRepository.findById(1L).get();
        Challenge challenge2 = challengeRepository.findById(2L).get();
        Challenge challenge3 = challengeRepository.findById(3L).get();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge3, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge4, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge5, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge1, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge1, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        cycleRepository.saveAll(List.of(
                inProgress1, inProgress2, inProgress3, failed, success, proceed1, proceed2));

        // when
        List<CycleResponse> actual = cycleService.findAllInProgressOfMine(
                tokenPayload, today, PageRequest.of(1, 3));

        // then
        assertThat(actual)
                .map(CycleResponse::getCycleId)
                .containsExactly(proceed2.getId(), proceed1.getId());
    }

    @DisplayName("나의 모든 진행 중인 사이클을 최대 페이지를 초과하여 조회")
    @Test
    void findAllInProgress_pagingOverMaxPage() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId(), NICKNAME);
        LocalDateTime today = LocalDateTime.now();
        Challenge challenge1 = challengeRepository.findById(1L).get();
        Challenge challenge2 = challengeRepository.findById(2L).get();
        Challenge challenge3 = challengeRepository.findById(3L).get();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge3, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge4, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge5, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge1, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge1, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        cycleRepository.saveAll(List.of(
                inProgress1, inProgress2, inProgress3, failed, success, proceed1, proceed2));

        // when
        List<CycleResponse> actual = cycleService.findAllInProgressOfMine(
                tokenPayload, today, PageRequest.of(2, 3));

        // then
        assertThat(actual).isEmpty();
    }

    @DisplayName("나의 모든 사이클 갯수와 성공 사이클 갯수 조회")
    @Test
    void searchStat() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, PASSWORD, NICKNAME));
        TokenPayload tokenPayload = new TokenPayload(member.getId(), NICKNAME);
        Challenge challenge1 = challengeRepository.findById(1L).get();
        Challenge challenge2 = challengeRepository.findById(2L).get();
        Challenge challenge3 = challengeRepository.findById(3L).get();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        Cycle inProgress1 = new Cycle(member, challenge1, Progress.FIRST, LocalDateTime.now().minusHours(43L));
        Cycle inProgress2 = new Cycle(member, challenge2, Progress.NOTHING, LocalDateTime.now().minusHours(5L));
        Cycle inProgress3 = new Cycle(member, challenge3, Progress.NOTHING, LocalDateTime.now());
        Cycle proceed1 = new Cycle(member, challenge4, Progress.FIRST, LocalDateTime.now());
        Cycle proceed2 = new Cycle(member, challenge5, Progress.SECOND, LocalDateTime.now().minusHours(36L));
        Cycle failed = new Cycle(member, challenge1, Progress.NOTHING, LocalDateTime.now().minusHours(120));
        Cycle success = new Cycle(member, challenge1, Progress.SUCCESS, LocalDateTime.now().minusHours(1000));
        cycleRepository.saveAll(List.of(
                inProgress1, inProgress2, inProgress3, failed, success, proceed1, proceed2));

        // when
        StatResponse response = cycleService.searchStat(tokenPayload);

        // then
        assertAll(
                () -> assertThat(response.getTotalCount()).isEqualTo(7),
                () -> assertThat(response.getSuccessCount()).isEqualTo(1)
        );
    }
}
