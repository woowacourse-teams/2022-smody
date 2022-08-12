package com.woowacourse.smody.ui.admin.service;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.repository.MemberRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class VaadinMemberService implements SmodyVaddinService<Member> {

    private final MemberRepository memberRepository;
    @Override
    public void deleteById(Long id) {
        memberRepository.deleteById(id);
    }

    @Override
    public Member save(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    @Override
    public Optional<Member> findById(Long id) {
        return memberRepository.findById(id);
    }
}
