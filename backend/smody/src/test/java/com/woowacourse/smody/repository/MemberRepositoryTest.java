package com.woowacourse.smody.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String NICKNAME = "손수건";
    private static final String PICTURE = "사진";

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Member를 저장한다.")
    @Test
    void save() {
        // given
        Member member = new Member(EMAIL, NICKNAME, PICTURE);

        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved).isEqualTo(member);
    }
}
