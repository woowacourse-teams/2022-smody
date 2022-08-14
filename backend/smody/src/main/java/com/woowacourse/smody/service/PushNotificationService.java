package com.woowacourse.smody.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.dto.PushNotificationResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.repository.PushNotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PushNotificationService {

	private final PushNotificationRepository pushNotificationRepository;
	private final MemberService memberService;

	public List<PushNotificationResponse> findByMember(TokenPayload tokenPayload) {
		Member member = memberService.search(tokenPayload);
		// List<PushNotification> pushNotifications pushNotificationRepository.findByMemberAndPushStatus();
		return null;
	}
}
