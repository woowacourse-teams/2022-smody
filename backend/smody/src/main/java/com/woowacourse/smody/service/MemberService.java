package com.woowacourse.smody.service;

import java.util.UUID;

import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.MemberUpdateRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.ImageUploader;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final CycleRepository cycleRepository;
    private final ImageUploader imageUploader;

    public MemberResponse searchMyInfo(TokenPayload tokenPayload) {
        Member member = search(tokenPayload);
        return new MemberResponse(member);
    }

    @Transactional
    public void updateMyInfo(TokenPayload tokenPayload, MemberUpdateRequest updateRequest) {
        Member member = search(tokenPayload);
        member.updateNickname(updateRequest.getNickname());
        member.updateIntroduction(updateRequest.getIntroduction());
    }

    @Transactional
    public void withdraw(TokenPayload tokenPayload) {
        Member member = search(tokenPayload);
        cycleRepository.deleteByMember(member);
        memberRepository.delete(member);
    }

    public Member search(TokenPayload tokenPayload) {
        return memberRepository.findById(tokenPayload.getId())
                .orElseThrow(() -> new BusinessException(ExceptionData.NOT_FOUND_MEMBER));
    }

    @Transactional
    public void updateProfileImage(TokenPayload tokenPayload, MultipartFile profileImage) {
        Member member = search(tokenPayload);

        String imageUrl = member.getPicture();
        imageUploader.remove(imageUrl);

        String fileName = UUID.randomUUID().toString();
        String newImageUrl = imageUploader.upload(profileImage, generateFolderName(member), fileName);
        member.updatePicture(newImageUrl);
    }

    private String generateFolderName(Member member) {
        return "member_" + member.getId();
    }
}
