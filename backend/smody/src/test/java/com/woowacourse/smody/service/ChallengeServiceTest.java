package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@SpringBootTest
@Transactional
class ChallengeServiceTest {

    @Autowired
    private ChallengeService challengeService;

    @Autowired
    private ChallengeRepository challengeRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private CycleRepository cycleRepository;

    @DisplayName("성공한 챌린지를 조회")
    @Test
    void searchSuccessOfMine() {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");
        memberRepository.save(member);
        TokenPayload tokenPayload = new TokenPayload(member.getId(), member.getNickname().getValue());
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        Challenge challenge3 = challengeRepository.findById(3L).orElseThrow();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0,0,0);
        cycleRepository.save(new Cycle(member, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge1, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge3, Progress.SUCCESS, today.minusDays(9L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(20L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(23L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(26L)));
        cycleRepository.save(new Cycle(member, challenge5, Progress.SUCCESS, today.minusDays(30L)));

        // when
        List<SuccessChallengeResponse> responses = challengeService.searchSuccessOfMine(
                tokenPayload, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(5),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getSuccessCount)
                        .containsExactly(2, 1, 1, 3, 1),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getChallengeId)
                        .containsExactly(challenge2.getId(), challenge1.getId(), challenge3.getId(), challenge4.getId(), challenge5.getId())
        );
    }

    @DisplayName("성공한 챌린지를 0페이지의 3개만 조회")
    @Test
    void searchSuccessOfMine_pageFullSize() {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");
        memberRepository.save(member);
        TokenPayload tokenPayload = new TokenPayload(member.getId(), member.getNickname().getValue());
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        Challenge challenge3 = challengeRepository.findById(3L).orElseThrow();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0,0,0);
        cycleRepository.save(new Cycle(member, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge1, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge3, Progress.SUCCESS, today.minusDays(9L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(20L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(23L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(26L)));
        cycleRepository.save(new Cycle(member, challenge5, Progress.SUCCESS, today.minusDays(30L)));

        // when
        List<SuccessChallengeResponse> responses = challengeService.searchSuccessOfMine(
                tokenPayload, PageRequest.of(0, 3));

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(3),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getSuccessCount)
                        .containsExactly(2, 1, 1),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getChallengeId)
                        .containsExactly(challenge2.getId(), challenge1.getId(), challenge3.getId())
        );
    }

    @DisplayName("성공한 챌린지를 1페이지의 2개만 조회")
    @Test
    void searchSuccessOfMine_pagePartialSize() {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");
        memberRepository.save(member);
        TokenPayload tokenPayload = new TokenPayload(member.getId(), member.getNickname().getValue());
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        Challenge challenge3 = challengeRepository.findById(3L).orElseThrow();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0,0,0);
        cycleRepository.save(new Cycle(member, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge1, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge3, Progress.SUCCESS, today.minusDays(9L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(20L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(23L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(26L)));
        cycleRepository.save(new Cycle(member, challenge5, Progress.SUCCESS, today.minusDays(30L)));

        // when
        List<SuccessChallengeResponse> responses = challengeService.searchSuccessOfMine(
                tokenPayload, PageRequest.of(1, 3));

        // then
        assertAll(
                () -> assertThat(responses.size()).isEqualTo(2),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getSuccessCount)
                        .containsExactly(3, 1),
                () -> assertThat(responses)
                        .map(SuccessChallengeResponse::getChallengeId)
                        .containsExactly(challenge4.getId(), challenge5.getId())
        );
    }

    @DisplayName("성공한 챌린지의 최대 페이지를 넘어가는 조회")
    @Test
    void searchSuccessOfMine_pageOverMaxPage() {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");
        memberRepository.save(member);
        TokenPayload tokenPayload = new TokenPayload(member.getId(), member.getNickname().getValue());
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        Challenge challenge3 = challengeRepository.findById(3L).orElseThrow();
        Challenge challenge4 = challengeRepository.save(new Challenge("알고리즘 1일 1문제"));
        Challenge challenge5 = challengeRepository.save(new Challenge("JPA 스터디"));
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0,0,0);
        cycleRepository.save(new Cycle(member, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge1, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member, challenge2, Progress.SUCCESS, today.minusDays(6L)));
        cycleRepository.save(new Cycle(member, challenge3, Progress.SUCCESS, today.minusDays(9L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(20L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(23L)));
        cycleRepository.save(new Cycle(member, challenge4, Progress.SUCCESS, today.minusDays(26L)));
        cycleRepository.save(new Cycle(member, challenge5, Progress.SUCCESS, today.minusDays(30L)));

        // when
        List<SuccessChallengeResponse> responses = challengeService.searchSuccessOfMine(
                tokenPayload, PageRequest.of(2, 3));

        // then
        assertThat(responses).isEmpty();
    }

    @DisplayName("모든 챌린지를 참여 중인 사람 수 기준 내림차순으로 정렬")
    @Test
    void findAllWithChallengerCount_sort() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member3, challenge1, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member1, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge2, Progress.SUCCESS, today.minusDays(3L)));

        // when
        List<ChallengeResponse> challengeResponses = challengeService.findAllWithChallengerCount(
                today, PageRequest.of(0, 10));

        // then
        assertAll(
                () -> assertThat(challengeResponses.size()).isEqualTo(3),
                () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeName))
                        .containsExactly("미라클 모닝", "스모디 방문하기", "오늘의 운동"),
                () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                        .containsExactly(2, 1, 0)
        );
    }

    @DisplayName("모든 챌린지를 참여 중인 사람 수 기준 내림차순으로 정렬 후 0페이지의 2개만 조회")
    @Test
    void findAllWithChallengerCount_pageFullSize() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member3, challenge1, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member1, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge2, Progress.SUCCESS, today.minusDays(3L)));

        // when
        List<ChallengeResponse> challengeResponses = challengeService.findAllWithChallengerCount(
                today, PageRequest.of(0, 2));

        // then
        assertAll(
                () -> assertThat(challengeResponses.size()).isEqualTo(2),
                () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeName))
                        .containsExactly("미라클 모닝", "스모디 방문하기"),
                () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                        .containsExactly(2, 1)
        );
    }

    @DisplayName("모든 챌린지를 참여 중인 사람 수 기준 내림차순으로 정렬 후 1페이지의 1개만 조회")
    @Test
    void findAllWithChallengerCount_pagePartialSize() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member3, challenge1, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member1, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge2, Progress.SUCCESS, today.minusDays(3L)));

        // when
        List<ChallengeResponse> challengeResponses = challengeService.findAllWithChallengerCount(
                today, PageRequest.of(1, 2));

        // then
        assertAll(
                () -> assertThat(challengeResponses.size()).isEqualTo(1),
                () -> assertThat(challengeResponses.stream().map(ChallengeResponse::getChallengeName))
                        .containsExactly("오늘의 운동"),
                () -> assertThat(challengeResponses.stream().mapToInt(ChallengeResponse::getChallengerCount))
                        .containsExactly(0)
        );
    }

    @DisplayName("모든 챌린지를 참여 중인 사람 수 기준 내림차순으로 정렬 후 최대 페이지를 초과한 페이지 조회")
    @Test
    void findAllWithChallengerCount_pageOverMaxPage() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge1 = challengeRepository.findById(1L).orElseThrow();
        Challenge challenge2 = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge1, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge1, Progress.FIRST, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member3, challenge1, Progress.SUCCESS, today.minusDays(3L)));
        cycleRepository.save(new Cycle(member1, challenge2, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge2, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge2, Progress.SUCCESS, today.minusDays(3L)));

        // when
        List<ChallengeResponse> challengeResponses = challengeService.findAllWithChallengerCount(
                today, PageRequest.of(2, 2));

        // then
        assertThat(challengeResponses).isEmpty();
    }

    @DisplayName("하나의 챌린지를 상세 조회")
    @Test
    void findOneWithChallengerCount() {
        // given
        Member member1 = new Member("alpha@naver.com", "abcde12345", "손수건");
        Member member2 = new Member("beta@naver.com", "abcde67890", "냅킨");
        Member member3 = new Member("gamma@naver.com", "fghij67890", "티슈");
        Challenge challenge = challengeRepository.findById(2L).orElseThrow();
        memberRepository.save(member1);
        memberRepository.save(member2);
        memberRepository.save(member3);
        LocalDateTime today = LocalDateTime.of(2022, 1, 1, 0, 0);
        cycleRepository.save(new Cycle(member1, challenge, Progress.NOTHING, today));
        cycleRepository.save(new Cycle(member2, challenge, Progress.FIRST, today.minusDays(1L)));
        cycleRepository.save(new Cycle(member3, challenge, Progress.SUCCESS, today.minusDays(3L)));

        // when
        ChallengeResponse challengeResponse = challengeService.findOneWithChallengerCount(today, challenge.getId());

        // then
        assertAll(
                () -> assertThat(challengeResponse.getChallengerCount()).isEqualTo(2),
                () -> assertThat(challengeResponse.getChallengeName()).isEqualTo(challenge.getName())
        );
    }
}
