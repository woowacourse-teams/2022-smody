package com.woowacourse.smody.auth.login;

import com.woowacourse.smody.auth.token.JwtTokenExtractor;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class AuthInterceptor implements HandlerInterceptor {

    private final JwtTokenExtractor jwtTokenExtractor;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        if (HttpMethod.OPTIONS.matches(request.getMethod())) {
            return true;
        }
        if (handler instanceof HandlerMethod) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequiredLogin requiredLogin = handlerMethod.getMethodAnnotation(RequiredLogin.class);
            if (Objects.isNull(requiredLogin)) {
                return true;
            }
            String token = jwtTokenExtractor.extract(request);
            validateToken(token);
            request.setAttribute("payload", jwtTokenProvider.getPayload(token));
            return true;
        }
        return true;
    }

    private void validateToken(String token) {
        if (!jwtTokenProvider.validateToken(token)) {
            throw new BusinessException(ExceptionData.INVALID_TOKEN);
        }
    }
}
