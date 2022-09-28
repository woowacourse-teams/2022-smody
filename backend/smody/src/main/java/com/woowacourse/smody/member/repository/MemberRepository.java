package com.woowacourse.smody.member.repository;

import com.woowacourse.smody.member.domain.Member;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, DynamicMemberRepository {

    Optional<Member> findByEmail(String email);
}
