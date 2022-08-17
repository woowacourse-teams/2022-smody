package com.woowacourse.smody.service;

import static com.woowacourse.smody.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.IntegrationTest;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Image;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.dto.MemberResponse;
import com.woowacourse.smody.dto.MemberUpdateRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.PushNotificationRepository;
import com.woowacourse.smody.repository.PushSubscriptionRepository;
import java.time.LocalDateTime;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

public class MemberServiceTest extends IntegrationTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @PersistenceContext
    private EntityManager em;

    private final Image progressImage = new Image(
            new MockMultipartFile("progressImage", "image".getBytes()),
            image -> "fakeUrl"
    );

    @DisplayName("자신의 회원 정보 조회를 한다.")
    @Test
    void searchMyInfo() {
        // when
        Member member = fixture.회원_조회(조조그린_ID);
        MemberResponse memberResponse = memberService.searchMyInfo(new TokenPayload(조조그린_ID));

        // then
        assertAll(
                () -> assertThat(memberResponse.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(memberResponse.getNickname()).isEqualTo(member.getNickname()),
                () -> assertThat(memberResponse.getPicture()).isEqualTo(member.getPicture())
        );
    }

    @DisplayName("자신의 회원 정보 조회할 때 회원이 존재하지 않는 경우 예외를 발생시킨다.")
    @Test
    void searchMyInfo_notExist() {
        // when then
        assertThatThrownBy(() -> memberService.searchMyInfo(new TokenPayload(Long.MAX_VALUE)))
                .isInstanceOf(BusinessException.class)
                .extracting("exceptionData")
                .isEqualTo(ExceptionData.NOT_FOUND_MEMBER);
    }

    @DisplayName("자신의 회원 정보를 수정한다.")
    @Test
    void updateMyInfo() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MemberUpdateRequest updateRequest = new MemberUpdateRequest("쬬그린", "나는 쬬그린");

        // when
        memberService.updateMyInfo(tokenPayload, updateRequest);

        // then
        Member findMember = fixture.회원_조회(조조그린_ID);
        assertAll(
                () -> assertThat(findMember.getNickname()).isEqualTo(updateRequest.getNickname()),
                () -> assertThat(findMember.getIntroduction()).isEqualTo(updateRequest.getIntroduction())
        );
    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    void withdraw() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        Cycle cycle = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        cycle.increaseProgress(LocalDateTime.now(), progressImage, "인증 완료");
        fixture.알림_구독(조조그린_ID, "endpoint");
        fixture.발송_예정_알림_생성(조조그린_ID, null, LocalDateTime.now(), PushCase.SUBSCRIPTION);

        // when
        memberService.withdraw(tokenPayload);
        em.flush();
        em.clear();

        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.searchMyInfo(tokenPayload))
                        .isInstanceOf(BusinessException.class),
                () -> assertThat(cycleRepository.findAll())
                        .hasSize(0),
                () -> assertThat(em.createQuery("select cd from CycleDetail cd").getResultList())
                        .hasSize(0),
                () -> assertThat(pushNotificationRepository.findAll()).hasSize(0),
                () -> assertThat(pushSubscriptionRepository.findAll()).hasSize(0)
        );
    }

    @DisplayName("회원을 프로필 이미지를 수정한다.")
    @Test
    void updateProfileImage() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        MultipartFile profileImage = new MockMultipartFile(
                "profileImage", "profile.jpg", "image/jpg", "image".getBytes()
        );
        String expected = "https://www.abc.com/profile.jpg";
        given(imageStrategy.extractUrl(any()))
                .willReturn(expected);

        // when
        memberService.updateProfileImage(tokenPayload, profileImage);

        // then
        assertThat(fixture.회원_조회(조조그린_ID).getPicture()).isEqualTo(expected);
    }
}
