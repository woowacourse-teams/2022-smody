package com.woowacourse.smody.push.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.dto.PushResponse;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.Security;
import java.util.concurrent.ExecutionException;
import lombok.extern.slf4j.Slf4j;
import nl.martijndwars.webpush.Notification;
import nl.martijndwars.webpush.PushService;
import org.apache.http.HttpResponse;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jose4j.lang.JoseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

	public boolean sendNotification(PushSubscription pushSubscription, PushNotification pushNotification) {
		PushResponse pushResponse = new PushResponse(pushNotification);
		HttpResponse httpResponse;
		try {
			httpResponse = pushService.send(new Notification(
				pushSubscription.getEndpoint(),
				pushSubscription.getP256dh(),
				pushSubscription.getAuth(),
				objectMapper.writeValueAsString(pushResponse)
			));
		} catch (InterruptedException exception) {
			log.warn("스레드가 interrupted 되었습니다.");
			Thread.currentThread().interrupt();
			throw new BusinessException(ExceptionData.WEB_PUSH_ERROR);
		}  catch (GeneralSecurityException | IOException
			| JoseException | ExecutionException exception) {
			throw new BusinessException(ExceptionData.WEB_PUSH_ERROR);
		}
		return httpResponse != null && httpResponse.getStatusLine().getStatusCode() == 201;
	}

    public String getPublicKey() {
        return publicKey;
    }
}