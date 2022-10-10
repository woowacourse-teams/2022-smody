package com.woowacourse.smody.member.service;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.member.dto.SearchedMemberResponse;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberApiService {

    private final MemberService memberService;
    private final ImageStrategy imageStrategy;

    public MemberResponse searchMyInfo(TokenPayload tokenPayload) {
        Member member = memberService.search(tokenPayload.getId());
        return new MemberResponse(member);
    }

    public List<SearchedMemberResponse> findAll(PagingParams pagingParams) {
        List<Member> members = memberService.findAll(pagingParams);
        return members.stream()
                .map(SearchedMemberResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void updateMyInfo(TokenPayload tokenPayload, MemberUpdateRequest updateRequest) {
        memberService.updateMyInfo(
                tokenPayload.getId(), updateRequest.getNickname(), updateRequest.getIntroduction()
        );
    }

    @Transactional
    public void updateProfileImage(TokenPayload tokenPayload, MultipartFile profileImage) {
        Image image = new Image(profileImage, imageStrategy);
        memberService.updateProfileImage(tokenPayload.getId(), image);
    }

    @Transactional
    public void withdraw(TokenPayload tokenPayload) {
        memberService.withdraw(tokenPayload.getId());
    }
}
