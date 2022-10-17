package com.woowacourse.smody.member.service;

import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MemberApiServiceTest extends IntegrationTest {

    @Autowired
    private MemberApiService memberApiService;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void withdraw() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.회원_조회(조조그린_ID);
        Cycle cycle1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now);
        Cycle cycle2 = fixture.사이클_생성_FIRST(조조그린_ID, 오늘의_운동_ID, now);
        fixture.댓글_등록(cycle1.getLatestCycleDetail(), 조조그린_ID, "댓글");
        fixture.댓글_등록(cycle2.getLatestCycleDetail(), 조조그린_ID, "댓글");
        fixture.발송_예정_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);
        fixture.알림_구독(조조그린_ID, "endpoint");

        // when
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        memberApiService.withdraw(tokenPayload);

        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberApiService.findByMe(tokenPayload))
                        .isInstanceOf(BusinessException.class),
                () -> assertThat(cycleRepository.findAll()).isEmpty(),
                () -> assertThat(em.createQuery("select cd from CycleDetail cd").getResultList()).isEmpty(),
                () -> assertThat(pushNotificationRepository.findAll()).isEmpty(),
                () -> assertThat(pushSubscriptionRepository.findAll()).isEmpty(),
                () -> assertThat(commentRepository.findAll()).isEmpty(),
                () -> assertThat(rankingActivityRepository.findAll()).isEmpty()
        );
    }
}
