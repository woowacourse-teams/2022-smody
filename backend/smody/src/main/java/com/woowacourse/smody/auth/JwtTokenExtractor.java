package com.woowacourse.smody.auth;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenExtractor {

    public static String BEARER_TYPE = "Bearer";

    public String extract(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (isBearerToken(token)) {
            return token.substring(BEARER_TYPE.length()).trim();
        }
        throw new BusinessException(ExceptionData.INVALID_TOKEN);
    }

    private boolean isBearerToken(String bearerToken) {
        return bearerToken != null
                && bearerToken.toLowerCase().startsWith(BEARER_TYPE.toLowerCase());
    }
}
