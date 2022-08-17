package com.woowacourse.smody.push.strategy;

import static com.woowacourse.smody.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Comment;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.repository.PushNotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class CommentPushStrategyTest extends IntegrationTest {

	@Autowired
	private CommentPushStrategy pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	private  CycleDetail cycleDetail;

	@BeforeEach
	void init() {
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		cycleDetail = cycle.getCycleDetails().get(0);
	}

	@DisplayName("댓글을 남기면 피드 작성자에게 발송 상태의 알림이 저장 된다.")
	@Test
	void push() {
		// given
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

	@DisplayName("알림을 구독했으면 댓글을 달았을 때 알림 저장에 전송까지 실행된다.")
	@Test
	void push_existSubscription() {
		// given
		fixture.알림_구독(조조그린_ID, "endpoint");
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
			() -> verify(webPushService)
				.sendNotification(any(), any())
		);
	}

	@DisplayName("자신의 게시글에 댓글을 달면 알림이 전송, 저장되지 않는다.")
	@Test
	void push_commentMyFeed() {
		// given
		Comment comment = fixture.댓글_등록(cycleDetail, 조조그린_ID, "댓글입니다.");
		fixture.알림_구독(조조그린_ID, "endpoint");

		// when
		pushStrategy.push(comment);

		// then
		List<PushNotification> results = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE);
		assertAll(
			() -> assertThat(results).isEmpty(),
			() -> verify(webPushService, never())
				.sendNotification(any(), any())
		);
	}
}
