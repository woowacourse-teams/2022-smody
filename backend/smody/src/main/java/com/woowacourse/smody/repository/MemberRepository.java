package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.member.Email;
import com.woowacourse.smody.domain.member.Member;
import com.woowacourse.smody.domain.member.Nickname;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    boolean existsByNickname(Nickname nickname);

    Optional<Member> findByEmail(Email email);
}
