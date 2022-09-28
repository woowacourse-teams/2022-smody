package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.domain.CommentCreateEvent;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.service.CommentService;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleCreateEvent;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushSubscribeEvent;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.support.IntegrationTest;

@DisplayName("알림 이벤트에 예외가 발생해도 ")
class EventExceptionTest extends IntegrationTest {

	@Autowired
	private CycleService cycleService;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private PushSubscriptionService pushSubscriptionService;

	@Autowired
	private CommentService commentService;

	@Autowired
	private ThreadPoolTaskExecutor executor;

	@MockBean
	private ChallengePushEventListener challengePushStrategy;

	@MockBean
	private SubscriptionPushEventListener subscriptionPushStrategy;

	@MockBean
	private CommentPushEventListener commentPushStrategy;

	@DisplayName("새로운 사이클이 저장된다.")
	@Test
	void cycleCreate_pushEventException() throws InterruptedException {
		// given
		LocalDateTime now = LocalDateTime.now();

		willThrow(new RuntimeException("알림 로직에 예상치 못한 예외 발생!"))
			.given(challengePushStrategy).handle(any(CycleCreateEvent.class));

		// when
		Long cycleId = cycleService.create(
			new TokenPayload(조조그린_ID),
			new CycleRequest(now, 스모디_방문하기_ID)
		);

		executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

		// then
		Optional<Cycle> cycle = cycleService.findById(cycleId);
		List<PushNotification> notifications = pushNotificationRepository.findAll();
		assertAll(
			() -> assertThat(cycle).isPresent(),
			() -> assertThat(notifications).isEmpty()
		);
	}

	@DisplayName("구독 정보는 저장된다.")
	@Test
	void subscribe_pushNotification() throws InterruptedException {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		willThrow(new RuntimeException("알림 로직에 예상치 못한 예외 발생!"))
			.given(subscriptionPushStrategy).handle(any(PushSubscribeEvent.class));

		// when
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);

		executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

		// then
		List<PushSubscription> subscriptions = pushSubscriptionService.searchByMembers(
			List.of(fixture.회원_조회(조조그린_ID))
		);
		List<PushNotification> notifications = pushNotificationRepository.findAll();
		assertAll(
			() -> assertThat(subscriptions).hasSize(1),
			() -> assertThat(subscriptions.get(0).getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(notifications).isEmpty()
		);
	}

	@DisplayName("댓글은 저장된다.")
	@Test
	void createComment_push() throws InterruptedException {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		CommentRequest commentRequest = new CommentRequest("댓글입니다");

		willThrow(new RuntimeException("알림 로직에 예상치 못한 예외 발생!"))
			.given(commentPushStrategy).handle(any(CommentCreateEvent.class));

		// when
		Long commentId = commentService.create(new TokenPayload(더즈_ID), cycleDetail.getId(), commentRequest);

		executor.getThreadPoolExecutor().awaitTermination(1, TimeUnit.SECONDS);

		// then
		List<PushNotification> notifications = pushNotificationRepository.findAll();
		Comment comment = commentService.search(commentId);
		assertAll(
			() -> assertThat(comment.getMember().getId()).isEqualTo(더즈_ID),
			() -> assertThat(notifications).isEmpty()
		);
	}
}
