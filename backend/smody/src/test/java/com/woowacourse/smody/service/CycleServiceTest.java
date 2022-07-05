package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.CycleResponse;
import com.woowacourse.smody.dto.SignUpRequest;
import com.woowacourse.smody.dto.SignUpResponse;
import com.woowacourse.smody.dto.TokenPayload;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CycleServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private CycleService cycleService;

    @Autowired
    private MemberService memberService;

    @DisplayName("사이클을 생성한다.")
    @Test
    void create() {
        // given
        SignUpResponse signUpResponse = memberService.signUp(new SignUpRequest(EMAIL, PASSWORD, NICKNAME));

        // when
        Long cycleId = cycleService.create(
                new TokenPayload(signUpResponse.getId(), NICKNAME),
                new CycleRequest(LocalDateTime.now(), 1L)
        );
        CycleResponse cycleResponse = cycleService.findById(cycleId);

        // then
        assertAll(
                () -> assertThat(cycleResponse.getCycleId()).isEqualTo(cycleId),
                () -> assertThat(cycleResponse.getChallengeId()).isEqualTo(1L)
        );
    }
}
