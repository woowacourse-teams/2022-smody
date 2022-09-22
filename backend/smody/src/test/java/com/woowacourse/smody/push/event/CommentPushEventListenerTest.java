package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.service.CommentService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

class CommentPushEventListenerTest extends IntegrationTest {

	@Autowired
	private CommentService commentService;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@DisplayName("댓글을 작성하면 알림이 저장 된다.")
	@Test
	void createComment_push() throws InterruptedException {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		CommentRequest commentRequest = new CommentRequest("댓글입니다");

		// when
		commentService.create(new TokenPayload(더즈_ID), cycleDetail.getId(), commentRequest);

		executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

		// then
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("더즈님께서 회원님의 피드에 댓글을 남겼어요!"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.COMMENT),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycleDetail.getId())
		);
	}

	@DisplayName("자신의 댓글을 작성하면 알림이 저장되지 않는다.")
	@Test
	void createComment_notPush() throws InterruptedException {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		CommentRequest commentRequest = new CommentRequest("댓글입니다");

		// when
		commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(), commentRequest);

		executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

		// then
		List<PushNotification> results = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE);
		assertThat(results).isEmpty();
	}
}
