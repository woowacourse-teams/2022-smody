package com.woowacourse.smody.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.domain.PushContent;
import com.woowacourse.smody.dto.SubscriptionRequestDto;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.dto.UnSubscriptionRequestDto;
import com.woowacourse.smody.repository.PushSubscriptionRepository;

import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@Service
public class WebPushService {

	private final PushService pushService;
	private final ObjectMapper objectMapper;
	private final PushSubscriptionRepository pushSubscriptionRepository;
	private final MemberService memberService;

	public WebPushService(
		@Value("${vapid.public.key}") String publicKey,
		@Value("${vapid.private.key}") String privateKey,
		PushSubscriptionRepository pushSubscriptionRepository,
		MemberService memberService) throws GeneralSecurityException {
		Security.addProvider(new BouncyCastleProvider());
		this.pushSubscriptionRepository = pushSubscriptionRepository;
		this.memberService = memberService;
		this.pushService = new PushService(publicKey, privateKey);
		this.objectMapper = new ObjectMapper();
	}

	public String getPublicKey() {
		return pushService.getPublicKey().toString();
	}

	@Transactional
	public void subscribe(TokenPayload tokenPayload, SubscriptionRequestDto subscriptionRequest) {
		Member member = memberService.search(tokenPayload);
		PushSubscription subscription = pushSubscriptionRepository.findByEndpoint(subscriptionRequest.endpoint)
			.map(pushSubscription -> pushSubscription.updateMember(member))
			.orElseGet(() -> pushSubscriptionRepository.save(subscriptionRequest.toEntity(member)));

		sendNotification(subscription, PushContent.builder()
			.title("구독 완료")
			.message(member.getNickname() + "님 스모디 알림이 구독되었습니다.")
			.build());
	}

	public void sendNotification(PushSubscription pushSubscription, PushContent pushContent) {
		try {
			pushService.send(new Notification(
				pushSubscription.getEndpoint(),
				getPublicKey(),
				pushSubscription.getAuth(),
				objectMapper.writeValueAsString(pushContent)
			));
		} catch (GeneralSecurityException | IOException
			| JoseException | ExecutionException | InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Transactional
	public void unSubscribe(TokenPayload tokenPayload, UnSubscriptionRequestDto unSubscription) {
		memberService.search(tokenPayload);
		pushSubscriptionRepository.deleteByEndpoint(unSubscription.getEndpoint());
	}
}
