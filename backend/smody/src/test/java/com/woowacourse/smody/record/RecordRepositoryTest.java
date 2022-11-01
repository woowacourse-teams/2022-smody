package com.woowacourse.smody.record;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.feed.repository.FeedRepository;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.record.dto.ChallengersResult;
import com.woowacourse.smody.record.dto.InProgressResult;
import com.woowacourse.smody.record.repository.RecordRepository;
import com.woowacourse.smody.support.IntegrationTest;
import com.woowacourse.smody.support.RepositoryTest;
import com.woowacourse.smody.support.ResourceFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.time.LocalDateTime;
import java.util.List;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class RecordRepositoryTest extends RepositoryTest {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private ResourceFixture resourceFixture;

    @PersistenceContext
    private EntityManager entityManager;

    @DisplayName("챌린지 10개에 대한 참여자 수를 가져온다")
    @Test
    void countChallengers() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        resourceFixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, startTime);
        resourceFixture.사이클_생성_FIRST(토닉_ID, 미라클_모닝_ID, startTime);
        resourceFixture.사이클_생성_FIRST(알파_ID, 알고리즘_풀기_ID, startTime);
        Challenge challenge1 = resourceFixture.챌린지_조회(미라클_모닝_ID);
        Challenge challenge2 = resourceFixture.챌린지_조회(알고리즘_풀기_ID);

        // when
        List<Long[]> challengersResults = recordRepository.countChallengers(List.of(challenge1, challenge2), startTime);

        // then
        assertAll(
                () -> assertThat(challengersResults.size()).isEqualTo(2),
                () -> assertThat(challengersResults.get(0)[0]).isEqualTo(미라클_모닝_ID),
                () -> assertThat(challengersResults.get(0)[1]).isEqualTo(2L),
                () -> assertThat(challengersResults.get(1)[0]).isEqualTo(알고리즘_풀기_ID),
                () -> assertThat(challengersResults.get(1)[1]).isEqualTo(1L)
        );
    }

    @DisplayName("챌린지 10개에 대한 멤버의 진행 여부를 가져온다")
    @Test
    void isInProgress() {
        // given
        LocalDateTime startTime = LocalDateTime.now();
        resourceFixture.사이클_생성_FIRST(더즈_ID, 미라클_모닝_ID, startTime); // 진행 중
        resourceFixture.사이클_생성_SUCCESS(더즈_ID, 미라클_모닝_ID, startTime.minusDays(3L)); // 과거에 성공
        resourceFixture.사이클_생성_SECOND(더즈_ID, 미라클_모닝_ID, startTime.minusDays(6L)); // 과거에 실패
        resourceFixture.사이클_생성_SECOND(더즈_ID, 오늘의_운동_ID, startTime); // 진행 중
        resourceFixture.사이클_생성_SUCCESS(더즈_ID, 오늘의_운동_ID, startTime.minusDays(3L)); // 과거에 성공
        resourceFixture.사이클_생성_SUCCESS(더즈_ID, 오늘의_운동_ID, startTime.minusDays(6L)); // 과거에 성공
        resourceFixture.사이클_생성_NOTHING(더즈_ID, 알고리즘_풀기_ID, startTime.minusDays(6L)); // 과거에 성공
        Challenge challenge1 = resourceFixture.챌린지_조회(미라클_모닝_ID);
        Challenge challenge2 = resourceFixture.챌린지_조회(알고리즘_풀기_ID);
        Challenge challenge3 = resourceFixture.챌린지_조회(오늘의_운동_ID);
        Member member = resourceFixture.회원_조회(더즈_ID);

        // when
        List<InProgressResult> inProgress = recordRepository.isInProgress(member, List.of(challenge1, challenge2, challenge3), startTime);

        // then
        assertAll(
                () -> assertThat(inProgress.size()).isEqualTo(2),
                () -> assertThat(inProgress.get(0).getChallengeId()).isEqualTo(미라클_모닝_ID),
                () -> assertThat(inProgress.get(0).isInProgress()).isTrue(),
                () -> assertThat(inProgress.get(1).getChallengeId()).isEqualTo(오늘의_운동_ID),
                () -> assertThat(inProgress.get(1).isInProgress()).isTrue()
        );
    }
}
