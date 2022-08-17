package com.woowacourse.smody.service;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.PushNotificationRepository;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

	private final PushNotificationRepository pushNotificationRepository;
	private final MemberService memberService;

	@Transactional
	public PushNotification register(PushNotification pushNotification) {
		return pushNotificationRepository.save(pushNotification);
	}

	public List<PushNotificationResponse> searchNotificationsOfMine(TokenPayload tokenPayload) {
		Member member = memberService.search(tokenPayload);
		return pushNotificationRepository.findAllLatest(member, PushStatus.COMPLETE)
			.stream()
			.map(PushNotificationResponse::new)
			.collect(Collectors.toList());
	}

	@Transactional
	public void delete(Long id) {
		pushNotificationRepository.deleteById(id);
	}

	public Optional<PushNotification> searchSamePathAndStatus(Long pathId, PushStatus status) {
		return pushNotificationRepository.findByPathIdAndPushStatus(pathId, status);
	}
}
