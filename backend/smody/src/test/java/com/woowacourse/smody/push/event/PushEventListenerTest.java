package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.doThrow;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.CycleDetail;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.dto.CommentRequest;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.SubscriptionRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.CommentService;
import com.woowacourse.smody.service.CycleService;
import com.woowacourse.smody.service.PushSubscriptionService;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class PushEventListenerTest extends IntegrationTest {

	private  static final MultipartFile IMAGE = new MockMultipartFile(
		"progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
	);

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	private CycleService cycleService;

	@Autowired
	private PushSubscriptionService pushSubscriptionService;

	@Autowired
	private CommentService commentService;

	@DisplayName("새로운 사이클을 생성하면 발송 예정인 알림이 저장된다.")
	@Test
	void cycleCreate_pushNotification() {
		// given
		LocalDateTime now = LocalDateTime.now();

		// when
		Long pathId = cycleService.create(
			new TokenPayload(조조그린_ID),
			new CycleRequest(now, 스모디_방문하기_ID)
		);

		// then
		LocalDateTime pushTime = now
			.plusDays(1L)
			.minusHours(3L);
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("스모디 방문하기 인증까지 얼마 안남았어요~"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(pathId)
		);
	}

	@DisplayName("사이클을 진행하면 발송 예정인 알림이 저장된다.")
	@Test
	void increaseProgress_sendNotification() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now.minusDays(1L));

		given(imageStrategy.extractUrl(any()))
			.willReturn("fakeUrl");

		// when
		cycleService.increaseProgress(
			new TokenPayload(조조그린_ID),
			new ProgressRequest(cycle.getId(), now, IMAGE, "인증")
		);

		// then
		LocalDateTime pushTime = now
			.plusDays(2L)
			.minusHours(3L);
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("미라클 모닝 인증까지 얼마 안남았어요~"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycle.getId())
		);
	}

	@DisplayName("사이클을 성공하면 알림이 저장되지 않는다.")
	@Test
	void increaseProgressSuccess_sendNotification_no() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));

		given(imageStrategy.extractUrl(any()))
			.willReturn("fakeUrl");

		// when
		cycleService.increaseProgress(
			new TokenPayload(조조그린_ID),
			new ProgressRequest(cycle.getId(), now, IMAGE, "인증")
		);

		// then
		assertThat(pushNotificationRepository.findAll()).hasSize(0);
	}

	@DisplayName("알림을 구독하면 푸시 알람 내역이 발송된 상태로 저장된다.")
	@Test
	void subscribe_pushNotification() {
		// given
		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		// when
		pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest);

		// then
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.COMPLETE),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("조조그린님 스모디 알림이 구독되었습니다."),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.SUBSCRIPTION),
			() -> assertThat(pushNotification.getPathId()).isNull()
		);
	}

	@DisplayName("구독 알림이 요청될 때 예외가 발생하면 비즈니스 예외로 변환한다.")
	@Test
	void subscribe_libraryException() {
		// given
		doThrow(new BusinessException(ExceptionData.WEB_PUSH_ERROR))
			.when(webPushService).sendNotification(any(), any());

		TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
		SubscriptionRequest subscriptionRequest = new SubscriptionRequest(
			"endpoint-link", "p256dh", "auth");

		// when // then
		assertThatThrownBy(() -> pushSubscriptionService.subscribe(tokenPayload, subscriptionRequest))
			.isInstanceOf(BusinessException.class)
			.extracting("exceptionData")
			.isEqualTo(ExceptionData.WEB_PUSH_ERROR);
	}

	@DisplayName("댓글을 작성하면 알림이 저장 된다.")
	@Test
	void createComment_push() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		CommentRequest commentRequest = new CommentRequest("댓글입니다");

		// when
		commentService.create(new TokenPayload(더즈_ID), cycleDetail.getId(), commentRequest);

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
	void createComment_notPush() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now);
		CycleDetail cycleDetail = cycle.getCycleDetails().get(0);

		CommentRequest commentRequest = new CommentRequest("댓글입니다");

		// when
		commentService.create(new TokenPayload(조조그린_ID), cycleDetail.getId(), commentRequest);

		// then
		List<PushNotification> results = pushNotificationRepository.findByPushStatus(PushStatus.COMPLETE);
		assertThat(results).isEmpty();
	}
}
