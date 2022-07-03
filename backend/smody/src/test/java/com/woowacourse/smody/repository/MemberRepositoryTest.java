package com.woowacourse.smody.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.domain.Email;
import com.woowacourse.smody.domain.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    private static final String EMAIL = "alpha@naver.com";
    private static final String PASSWORD = "abcde12345";
    private static final String NICKNAME = "손수건";

    @Autowired
    private MemberRepository memberRepository;

    @DisplayName("Member를 저장한다.")
    @Test
    void save() {
        // given
        Member member = new Member(EMAIL, PASSWORD, NICKNAME);

        // when
        Member saved = memberRepository.save(member);

        // then
        assertThat(saved).isEqualTo(member);
    }

    @DisplayName("Email이 중복임을 확인한다.")
    @Test
    void existsByEmail() {
        // given
        Member member = new Member(EMAIL, PASSWORD, NICKNAME);
        memberRepository.save(member);

        // when
        boolean actual = memberRepository.existsByEmail(member.getEmail());

        // then
        assertThat(actual).isTrue();
    }

    @DisplayName("Email이 중복되지 않음을 확인한다.")
    @Test
    void notExistsByEmail() {
        // when
        boolean actual = memberRepository.existsByEmail(new Email(EMAIL));

        // then
        assertThat(actual).isFalse();
    }
}
