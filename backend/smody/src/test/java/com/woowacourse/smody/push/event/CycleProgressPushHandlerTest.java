package com.woowacourse.smody.push.event;

import static com.woowacourse.smody.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.time.LocalDateTime;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.ProgressRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.image.ImageStrategy;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.service.CycleService;

@SpringBootTest
@Transactional
class CycleProgressPushHandlerTest {

	@Autowired
	private PushNotificationRepository pushNotificationRepository;

	@Autowired
	@InjectMocks
	private CycleService cycleService;

	@MockBean
	private ImageStrategy imageStrategy;

	@Autowired
	private ResourceFixture fixture;

	@DisplayName("새로운 사이클을 생성하면 발송 예정인 알림이 저장된다.")
	@Test
	void cycleCreate_pushNotification() {
		// given
		LocalDateTime now = LocalDateTime.now();

		// when
		cycleService.create(
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
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime)
		);
	}

	@DisplayName("사이클을 진행하면 발송 예정인 알림이 저장된다.")
	@Test
	void increaseProgress_sendNotification() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_FIRST(조조그린_ID, 미라클_모닝_ID, now.minusDays(1L));

		MultipartFile imageFile = new MockMultipartFile(
			"progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
		);
		given(imageStrategy.extractUrl(any()))
			.willReturn("fakeUrl");

		// when
		cycleService.increaseProgress(
			new TokenPayload(조조그린_ID),
			new ProgressRequest(cycle.getId(), now, imageFile, "인증")
		);

		// then
		LocalDateTime pushTime = now
			.plusDays(2L)
			.minusHours(3L);
		PushNotification pushNotification = pushNotificationRepository.findAll().get(0);
		assertAll(
			() -> assertThat(pushNotification.getMember().getId()).isEqualTo(조조그린_ID),
			() -> assertThat(pushNotification.getPushStatus()).isEqualTo(PushStatus.IN_COMPLETE),
			() -> assertThat(pushNotification.getPushTime()).isEqualTo(pushTime)
		);
	}

	@DisplayName("사이클을 성공하면 알림이 저장되지 않는다.")
	@Test
	void increaseProgressSuccess_sendNotification_no() {
		// given
		LocalDateTime now = LocalDateTime.now();
		Cycle cycle = fixture.사이클_생성_SECOND(조조그린_ID, 미라클_모닝_ID, now.minusDays(2L));

		MultipartFile imageFile = new MockMultipartFile(
			"progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
		);
		given(imageStrategy.extractUrl(any()))
			.willReturn("fakeUrl");

		// when
		cycleService.increaseProgress(
			new TokenPayload(조조그린_ID),
			new ProgressRequest(cycle.getId(), now, imageFile, "인증")
		);

		// then
		assertThat(pushNotificationRepository.findAll()).hasSize(0);
	}
}
