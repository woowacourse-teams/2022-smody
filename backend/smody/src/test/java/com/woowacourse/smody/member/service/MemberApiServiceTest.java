package com.woowacourse.smody.member.service;

import static com.woowacourse.smody.support.ResourceFixture.미라클_모닝_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.이미지;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
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
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;

public class MemberApiServiceTest extends IntegrationTest {

    @Autowired
    private MemberApiService memberApiService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private CycleRepository cycleRepository;

    @Autowired
    private PushNotificationRepository pushNotificationRepository;

    @Autowired
    private PushSubscriptionRepository pushSubscriptionRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void findByMe() {
        // given
        Member member = fixture.회원_조회(조조그린_ID);

        // when
        MemberResponse actual = memberApiService.findByMe(new TokenPayload(조조그린_ID));

        // then
        assertAll(
                () -> assertThat(actual.getNickname()).isEqualTo(member.getNickname()),
                () -> assertThat(actual.getEmail()).isEqualTo(member.getEmail()),
                () -> assertThat(actual.getPicture()).isEqualTo(member.getPicture()),
                () -> assertThat(actual.getIntroduction()).isEqualTo(member.getIntroduction())
        );
    }

    @DisplayName("회원을 조회할 때")
    @Nested
    class FindAllByFilter {

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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(8),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(5),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(4),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(5),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(3),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(3),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
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
            List<SearchedMemberResponse> actual = memberApiService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(actual).hasSize(2),
                    () -> assertThat(actual).map(SearchedMemberResponse::getNickname)
                            .containsExactly("그랑조", "조")
            );
        }
    }

    @DisplayName("회원의 별명과 설명을 수정한다.")
    @Test
    void updateByMe() {
        // when
        memberApiService.updateByMe(new TokenPayload(조조그린_ID), new MemberUpdateRequest("토닉", "바뀜"));

        // then
        MemberResponse actual = memberApiService.findByMe(new TokenPayload(조조그린_ID));
        assertAll(
                () -> assertThat(actual.getNickname()).isEqualTo("토닉"),
                () -> assertThat(actual.getIntroduction()).isEqualTo("바뀜")
        );
    }

    @DisplayName("회원의 프로필 이미지를 수정한다.")
    @Test
    void updateProfileImageByMe() {
        // given
        fixture.회원_조회(조조그린_ID);
        MockMultipartFile updateImage = new MockMultipartFile(
                "updateProgressImage", "updateProgressImage.jpg", "image/jpg", "image".getBytes()
        );
        TokenPayload tokenPayload = new TokenPayload(조조그린_ID);
        given(imageStrategy.extractUrl(updateImage)).willReturn("updateProgressImage.jpg");

        // when
        memberApiService.updateProfileImageByMe(tokenPayload, updateImage);

        // then
        MemberResponse actual = memberApiService.findByMe(tokenPayload);
        assertThat(actual.getPicture()).isEqualTo("updateProgressImage.jpg");
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void withdraw() {
        // given
        Cycle cycle1 = fixture.사이클_생성_NOTHING(조조그린_ID, 미라클_모닝_ID, LocalDateTime.now());
        Cycle cycle2 = fixture.사이클_생성_NOTHING(조조그린_ID, 오늘의_운동_ID, LocalDateTime.now());
        cycle1.increaseProgress(LocalDateTime.now(), 이미지, "인증 완료");
        cycle2.increaseProgress(LocalDateTime.now(), 이미지, "인증 완료");
        fixture.알림_구독(조조그린_ID, "endpoint");
        fixture.발송_예정_알림_생성(조조그린_ID, null, LocalDateTime.now(), PushCase.SUBSCRIPTION);

        // when
        memberApiService.withdraw(new TokenPayload(조조그린_ID));

        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.search(조조그린_ID))
                        .isInstanceOf(BusinessException.class),
                () -> assertThat(cycleRepository.findAll())
                        .isEmpty(),
                () -> assertThat(em.createQuery("select cd from CycleDetail cd").getResultList())
                        .isEmpty(),
                () -> assertThat(pushNotificationRepository.findAll()).isEmpty(),
                () -> assertThat(pushSubscriptionRepository.findAll()).isEmpty()
        );
    }
}
