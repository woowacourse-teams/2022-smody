package com.woowacourse.smody.push.strategy;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.repository.PushNotificationRepository;

class CommentPushStrategyTest extends IntegrationTest {

	@Autowired
	private CommentPushStrategy pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@DisplayName("댓글을 남기면 피드 작성자에게 발송 상태의 알림이 저장 된다.")
	@Test
	void push() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		Comment comment = fixture.댓글_등록(cycleDetail, 더즈_ID, "댓글입니다.");

		// when
		pushStrategy.push(comment);

		// then
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(comment.getCreatedAt()),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("더즈님께서 회원님의 피드에 댓글을 남겼어요!"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.COMMENT),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycleDetail.getId()),
			() -> verify(webPushService, never())
				.sendNotification(any(), any())
		);
	}

	@DisplayName("알림을 구독했으면 알림 저장에 전송까지 실행된다.")
	@Test
	void push_existSubscription() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		Comment comment = fixture.댓글_등록(cycleDetail, 더즈_ID, "댓글입니다.");

		fixture.알림_구독(조조그린_ID, "endpoint");

		// when
		pushStrategy.push(comment);

		// then
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(comment.getCreatedAt()),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("더즈님께서 회원님의 피드에 댓글을 남겼어요!"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.COMMENT),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycleDetail.getId()),
			() -> verify(webPushService)
				.sendNotification(any(), any())
		);
	}
}
