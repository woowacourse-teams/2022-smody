package com.woowacourse.smody.ui.admin.controller;


import com.vaadin.flow.spring.security.VaadinWebSecurityConfigurerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@EnableWebSecurity
@Configuration
public class AdminSecurityConfig extends VaadinWebSecurityConfigurerAdapter {

    @Value("${admin.id}")
    private String adminId;
    @Value("${admin.password}")
    private String adminPassword;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/challenges/**", "/cycles/**", "/members/**", "/oauth/**", "/docs/**", "/images/**",
                "/feeds/**", "/comments/**", "/web-push/**", "/profile/**", "/h2-console/**", "/push-notifications/**",
                "/ranking-periods/**"
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser(adminId).password("{noop}" + adminPassword).roles("ADMIN");
    }
}
