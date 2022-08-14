package com.woowacourse.smody.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushStatus;
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

	public List<PushNotificationResponse> searchNotificationsOfMine(TokenPayload tokenPayload) {
		Member member = memberService.search(tokenPayload);
		return pushNotificationRepository.findAllLatest(member, PushStatus.COMPLETE)
			.stream()
			.map(PushNotificationResponse::new)
			.collect(Collectors.toList());
	}
}
