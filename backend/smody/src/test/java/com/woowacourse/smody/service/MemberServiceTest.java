package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ResourceFixture;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.MemberUpdateRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private ResourceFixture fixture;

    @DisplayName("자신의 회원 정보 조회를 한다.")
    @Test
    void searchMyInfo() {
        // when
        Member member = fixture.회원_조회(조조그린_ID);
        MemberResponse memberResponse = memberService.searchMyInfo(new TokenPayload(조조그린_ID));

        // then
        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname()),
                () -> assertThat(memberResponse.getPicture()).isEqualTo(member.getPicture())
        );
    }

    @DisplayName("자신의 회원 정보 조회할 때 회원이 존재하지 않는 경우 예외를 발생시킨다.")
    @Test
    void searchMyInfo_notExist() {
        // when then
        assertThatThrownBy(() -> memberService.searchMyInfo(new TokenPayload(Long.MAX_VALUE)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_MEMBER);
    }

    @DisplayName("자신의 회원 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("쬬그린", "나는 쬬그린", "이상해씨");

        // when
        memberService.updateMyInfo(tokenPayload, updateRequest);

        // then
        Member findMember = fixture.회원_조회(조조그린_ID);
        assertAll(
                () -> assertThat(findMember.getNickname()).isEqualTo(updateRequest.getNickname()),
                () -> assertThat(findMember.getIntroduction()).isEqualTo(updateRequest.getIntroduction()),
                () -> assertThat(findMember.getPicture()).isEqualTo(updateRequest.getPicture())
        );
    }
}
