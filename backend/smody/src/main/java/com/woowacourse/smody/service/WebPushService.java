package com.woowacourse.smody.service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.domain.PushContent;
import com.woowacourse.smody.domain.PushSubscription;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;

import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;

@Service
@Slf4j
public class WebPushService {

	private final String publicKey;

	private final PushService pushService;
	private final ObjectMapper objectMapper;

	public WebPushService(
		@Value("${vapid.public.key}") String publicKey,
		@Value("${vapid.private.key}") String privateKey) throws GeneralSecurityException {
		this.publicKey = publicKey;
		Security.addProvider(new BouncyCastleProvider());
		this.pushService = new PushService(publicKey, privateKey);
		this.objectMapper = new ObjectMapper();
	}

	public void sendNotification(PushSubscription pushSubscription, PushContent pushContent) {
		try {
			pushService.send(new Notification(
				pushSubscription.getEndpoint(),
				pushSubscription.getP256dh(),
				pushSubscription.getAuth(),
				objectMapper.writeValueAsString(pushContent)
			));
		} catch (GeneralSecurityException | IOException
			| JoseException | ExecutionException | InterruptedException e) {
			log.error("웹 푸시 라이브러리 관련 예외가 발생했습니다.");
			throw new BusinessException(ExceptionData.WEB_PUSH_ERROR);
		}
	}

	public String getPublicKey() {
		return publicKey;
	}
}
