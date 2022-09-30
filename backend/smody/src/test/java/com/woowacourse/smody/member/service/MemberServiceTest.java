package com.woowacourse.smody.member.service;

import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.member.dto.SearchedMemberResponse;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import com.woowacourse.smody.support.IntegrationTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

class MemberServiceTest extends IntegrationTest {

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
        Cycle cycle1 = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        Cycle cycle2 = fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, LocalDateTime.now());
        cycle1.increaseProgress(LocalDateTime.now(), 이미지, "인증 완료");
        cycle2.increaseProgress(LocalDateTime.now(), 이미지, "인증 완료");
        fixture.알림_구독(조조그린_ID, "endpoint");
        fixture.발송_예정_알림_생성(조조그린_ID, null, LocalDateTime.now(), PushCase.SUBSCRIPTION);

        // when
        memberService.withdraw(tokenPayload);

        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.searchMyInfo(tokenPayload))
                        .isInstanceOf(BusinessException.class),
                () -> assertThat(cycleRepository.findAll())
                        .isEmpty(),
                () -> assertThat(em.createQuery("select cd from CycleDetail cd").getResultList())
                        .isEmpty(),
                () -> assertThat(pushNotificationRepository.findAll()).isEmpty(),
                () -> assertThat(pushSubscriptionRepository.findAll()).isEmpty()
        );
    }

    @DisplayName("회원을 프로필 이미지를 수정한다.")
    @Test
    void updateProfileImage() {
        // given
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        String expected = "https://www.abc.com/profile.jpg";
        given(imageStrategy.extractUrl(any()))
                .willReturn(expected);

        // when
        memberService.updateProfileImage(tokenPayload, MULTIPART_FILE);

        // then
        assertThat(fixture.회원_조회(조조그린_ID).getPicture()).isEqualTo(expected);
    }

    @DisplayName("회원을 프로필 이미지와 닉네임, 소개를 다른 스레드에서 각각 수정하면 "
        + "전체가 다 수정되어야 한다.")
    @Test
    @Rollback(value = false)
    void updateMember_multiThread() throws InterruptedException {
        // given
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);

        given(imageStrategy.extractUrl(any()))
            .willReturn("update-image-url");

        // when
        service.execute(() -> {
            memberService.updateMyInfo(tokenPayload, new MemberUpdateRequest("쬬그린", "HI"));
            latch.countDown();
        });

        service.execute(() -> {
            memberService.updateProfileImage(tokenPayload, MULTIPART_FILE);
            latch.countDown();
        });

        latch.await();

        // then
        Member result = memberService.search(tokenPayload);
        assertAll(
            () -> assertThat(result.getPicture()).isEqualTo("update-image-url"),
            () -> assertThat(result.getNickname()).isEqualTo("쬬그린"),
            () -> assertThat(result.getIntroduction()).isEqualTo("HI")
        );
        rollbackToOriginalData(result);
    }

    private void rollbackToOriginalData(Member result) {
        result.updateNickname("조조그린");
        result.updatePicture(new Image(null,
                image -> "'https://lh3.googleusercontent.com/a-/AFdZucp2Jil0TsQ_Edr7hFi7RGfyJK48yeffjHgCVI3JNw=s96-c'"));
        result.updateIntroduction(null);
    }

    @DisplayName("회원을 조회할 때")
    @Nested
    class findMembersTest {

        @DisplayName("글자가 없고 커서 ID도 없고 크가가 10일떄")
        @Test
        void findAll() {
            // given
            PagingParams pagingParams = new PagingParams();
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(8),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조조그린", "더즈", "토닉", "알파", "조그린", "그랑조", "조", "양조장")
            );
        }

        @DisplayName("글자가 없고 커서 ID도 없고 크기가 5일때")
        @Test
        void findAll_sizeFive() {
            // given
            PagingParams pagingParams = new PagingParams(null, 5);
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(5),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조조그린", "더즈", "토닉", "알파", "조그린")
            );
        }

        @DisplayName("글자가 없고 커서 ID는 있고 크기가 10일 때")
        @Test
        void findAll_existCursorId() {
            // given
            PagingParams pagingParams = new PagingParams(null, 0, 알파_ID);
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(4),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조그린", "그랑조", "조", "양조장")
            );
        }

        @DisplayName("글자가 없고 커서 ID는 있고 크기가 2일 때")
        @Test
        void findAll_existCursorId_sizeTwo() {
            // given
            PagingParams pagingParams = new PagingParams(null, 2, 알파_ID);
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(2),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조그린", "그랑조")
            );
        }

        @DisplayName("글자가 있고 커서 ID는 없고 크기가 10일 때")
        @Test
        void findAll_existFilter() {
            // given
            PagingParams pagingParams = new PagingParams(null, 0, null, "조");
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(5),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조조그린", "조그린", "그랑조", "조", "양조장")
            );
        }

        @DisplayName("글자가 있고 커서 ID는 없고 크기가 3일 때")
        @Test
        void findAll_existFilter_sizeThree() {
            // given
            PagingParams pagingParams = new PagingParams(null, 3, null, "조");
            fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(3),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("조조그린", "조그린", "그랑조")
            );
        }

        @DisplayName("글자가 있고 커서 ID는 있고 크기가 10일 때")
        @Test
        void findAll_existFilter_existCursorId() {
            // given
            Member member = fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");
            PagingParams pagingParams = new PagingParams(null, 0, member.getId(), "조");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(3),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("그랑조", "조", "양조장")
            );
        }

        @DisplayName("글자가 있고 커서 ID는 있고 크기가 2일 때")
        @Test
        void findAll_existFilter_existCursorId_sizeTwo() {
            // given
            Member member = fixture.회원_추가("조그린", "a@naver.com");
            fixture.회원_추가("그랑조", "b@naver.com");
            fixture.회원_추가("조", "c@naver.com");
            fixture.회원_추가("양조장", "d@naver.com");
            PagingParams pagingParams = new PagingParams(null, 2, member.getId(), "조");

            // when
            List<SearchedMemberResponse> searchedMemberResponse = memberService.findAll(pagingParams);

            // then
            assertAll(
                    () -> assertThat(searchedMemberResponse.size()).isEqualTo(2),
                    () -> assertThat(searchedMemberResponse).map(SearchedMemberResponse::getNickname)
                            .containsExactly("그랑조", "조")
            );
        }
    }
}
