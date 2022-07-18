package com.woowacourse.smody.config;

import com.woowacourse.smody.auth.AuthInterceptor;
import com.woowacourse.smody.auth.LoginMemberArgumentResolver;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private static final List<String> AUTH_REQUIRED_URL = List.of(
            "/cycles", "/cycles/me", "/cycles/*/progress", "/cycles/me/stat", "/challenges/me", "/members/me"
    );

    @Value("${frontend.public-ip}")
    private String frontendPublicIp;

    private final AuthInterceptor authInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns(AUTH_REQUIRED_URL);
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendPublicIp)
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE");
    }
}
