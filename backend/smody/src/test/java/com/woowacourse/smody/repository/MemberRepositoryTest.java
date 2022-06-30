package com.woowacourse.smody.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.domain.Member;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public class MemberRepositoryTest {

    @Autowired
    private MemberRepository memberRepository;

    @Test
    void save() {
        // given
        Member member = new Member("alpha@naver.com", "abcde12345", "손수건");

        // when
        memberRepository.save(member);

        // then
        assertThat(member.getId()).isEqualTo(1L);
    }
}
