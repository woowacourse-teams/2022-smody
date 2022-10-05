package com.woowacourse.smody.auth.token;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

@Component
public class JwtTokenExtractor {

    private static final String BEARER_TYPE = "Bearer";

    public String extract(HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        validateBearerToken(token);
        return token.substring(BEARER_TYPE.length()).trim();
    }

    private void validateBearerToken(String bearerToken) {
        if (bearerToken == null
                || !bearerToken.toLowerCase().startsWith(BEARER_TYPE.toLowerCase())) {
            throw new BusinessException(ExceptionData.INVALID_TOKEN);
        }
    }
}
