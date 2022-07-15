package com.woowacourse.smody.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("자신의 회원 정보 조회를 한다.")
    @Test
    void searchMyInfo() {
        // given
        Member member = memberRepository.save(new Member(EMAIL, NICKNAME, PICTURE));

        // when
        MemberResponse memberResponse = memberService.searchMyInfo(
                new TokenPayload(member.getId()));

        // then
        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(EMAIL),
                () -> assertThat(memberResponse.getNickname()).isEqualTo(NICKNAME),
                () -> assertThat(memberResponse.getPicture()).isEqualTo(PICTURE)
        );
    }

    @DisplayName("자신의 회원 정보 조회할 때 회원이 존재하지 않는 경우 예외를 발생시킨다.")
    @Test
    void searchMyInfo_notExist() {
        // when then
        assertThatThrownBy(() -> memberService.searchMyInfo(new TokenPayload(100L)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_MEMBER);
    }
}
