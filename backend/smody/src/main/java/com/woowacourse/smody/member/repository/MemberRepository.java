package com.woowacourse.smody.member.repository;

import com.woowacourse.smody.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long>, DynamicMemberRepository {

    Optional<Member> findByEmail(String email);

    List<Member> findByIdIn(List<Long> id);
}
