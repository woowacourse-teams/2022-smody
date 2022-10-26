package com.woowacourse.smody.config;

import com.woowacourse.smody.auth.login.AuthInterceptor;
import com.woowacourse.smody.auth.login.LoginMemberArgumentResolver;
import com.woowacourse.smody.auth.token.TokenCheckerArgumentResolver;
import com.woowacourse.smody.logging.LogInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${frontend.origin}")
    private String frontendOrigin;

    private final AuthInterceptor authInterceptor;
    private final LogInterceptor logInterceptor;
    private final LoginMemberArgumentResolver loginMemberArgumentResolver;
    private final TokenCheckerArgumentResolver tokenCheckerArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(loginMemberArgumentResolver);
        resolvers.add(tokenCheckerArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(logInterceptor)
                .order(0)
                .addPathPatterns("/**");
        registry.addInterceptor(authInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(frontendOrigin)
                .allowedMethods("OPTIONS", "GET", "POST", "PUT", "DELETE", "PATCH");
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
