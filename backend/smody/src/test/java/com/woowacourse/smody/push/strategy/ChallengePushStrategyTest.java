package com.woowacourse.smody.push.strategy;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.repository.PushNotificationRepository;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

class ChallengePushStrategyTest extends IntegrationTest {

	@Autowired
	private ChallengePushStrategy pushStrategy;

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@DisplayName("챌린지 인증 임박 알림을 저장한다.")
	@Test
	void push() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);

		// when
		pushStrategy.push(cycle);

		// then
		LocalDateTime pushTime = now
			.plusDays(1L)
			.minusHours(3L);
		PushNotification pushNotification = pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE).get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime),
			() -> assertThat(pushNotification.getMessage()).contains("미라클 모닝 인증까지 얼마 안남았어요~"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycle.getId())
		);
	}

	@DisplayName("사이클을 진행해서 새로운 인증 임박 알림을 만들 때, "
		+ "같은 사이클의 이전 인증 임박 알림이 보내지지 않았다면 삭제한다.")
	@Test
	void push_cycleProgress() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, now);
		pushStrategy.push(cycle);

		MultipartFile imageFile = new MockMultipartFile(
			"progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
		);
		cycle.increaseProgress(now.plusMinutes(1L), new Image(imageFile, image -> "fakeUrl"), "인증");

		// when
		pushStrategy.push(cycle);

		// then
		LocalDateTime pushTime = now
			.plusDays(2L)
			.minusHours(3L);
		List<PushNotification> results = pushNotificationRepository.findByPushStatus(PushStatus.IN_COMPLETE);
		PushNotification pushNotification = results.get(0);
		assertAll(
			() -> assertThat(results).hasSize(1),
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime),
			() -> assertThat(pushNotification.getMessage()).isEqualTo("미라클 모닝 인증까지 얼마 안남았어요~"),
			() -> assertThat(pushNotification.getPushCase()).isEqualTo(PushCase.CHALLENGE),
			() -> assertThat(pushNotification.getPathId()).isEqualTo(cycle.getId())
		);
	}
}
