package com.woowacourse.smody.repository;

import com.woowacourse.smody.domain.Email;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Nickname;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    boolean existsByEmail(Email email);

    boolean existsByNickname(Nickname nickname);

    Optional<Member> findByEmail(Email email);
}
