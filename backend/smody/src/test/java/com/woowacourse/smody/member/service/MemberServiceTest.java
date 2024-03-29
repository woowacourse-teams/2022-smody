package com.woowacourse.smody.member.service;

import static com.woowacourse.smody.support.ResourceFixture.MULTIPART_FILE;
import static com.woowacourse.smody.support.ResourceFixture.더즈_ID;
import static com.woowacourse.smody.support.ResourceFixture.스모디_방문하기_ID;
import static com.woowacourse.smody.support.ResourceFixture.알파_ID;
import static com.woowacourse.smody.support.ResourceFixture.오늘의_운동_ID;
import static com.woowacourse.smody.support.ResourceFixture.조조그린_ID;
import static com.woowacourse.smody.support.ResourceFixture.토닉_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import com.woowacourse.smody.ranking.repository.RankingActivityRepository;
import com.woowacourse.smody.support.IntegrationTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
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

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private RankingActivityRepository rankingActivityRepository;

    @PersistenceContext
    private EntityManager em;

    @DisplayName("자신의 회원정보를 조회할 때")
    @Nested
    class Search {

        @DisplayName("조회")
        @Test
        void search() {
            // when
            Member expected = fixture.회원_조회(조조그린_ID);
            Member actual = memberService.search(조조그린_ID);

            // then
            assertAll(
                    () -> assertThat(actual.getEmail()).isEqualTo(expected.getEmail()),
                    () -> assertThat(actual.getNickname()).isEqualTo(expected.getNickname()),
                    () -> assertThat(actual.getPicture()).isEqualTo(expected.getPicture())
            );
        }

        @DisplayName("회원이 존재하지 않는 경우 예외를 발생시킨다.")
        @Test
        void searchMyInfo_notExist() {
            // when then
            assertThatThrownBy(() -> memberService.search(Long.MAX_VALUE))
                    .isInstanceOf(BusinessException.class)
                    .extracting("exceptionData")
                    .isEqualTo(ExceptionData.NOT_FOUND_MEMBER);
        }
    }


    @DisplayName("자신의 회원 정보를 수정한다.")
    @Test
    void updateByMe() {
        // when
        memberService.updateByMe(조조그린_ID, "쬬그린", "나는 쬬그린");

        // then
        Member findMember = fixture.회원_조회(조조그린_ID);
        assertAll(
                () -> assertThat(findMember.getNickname()).isEqualTo("쬬그린"),
                () -> assertThat(findMember.getIntroduction()).isEqualTo("나는 쬬그린")
        );
    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    void withdraw() {
        // given
        LocalDateTime now = LocalDateTime.now();
        fixture.회원_조회(조조그린_ID);
        Cycle cycle1 = fixture.사이클_생성_FIRST(조조그린_ID, 스모디_방문하기_ID, now);
        Cycle cycle2 = fixture.사이클_생성_FIRST(조조그린_ID, 오늘의_운동_ID, now);
        fixture.댓글_등록(cycle1.getLatestCycleDetail(), 조조그린_ID, "댓글");
        fixture.댓글_등록(cycle2.getLatestCycleDetail(), 조조그린_ID, "댓글");
        fixture.발송_예정_알림_생성(조조그린_ID, 1L, now, PushCase.CHALLENGE);
        fixture.알림_구독(조조그린_ID, "endpoint");

        // when
        memberService.withdraw(조조그린_ID);

        // then
        assertAll(
                () -> assertThatThrownBy(() -> memberService.search(조조그린_ID))
                        .isInstanceOf(BusinessException.class),
                () -> assertThat(cycleRepository.findAll()).isEmpty(),
                () -> assertThat(em.createQuery("select cd from CycleDetail cd").getResultList()).isEmpty(),
                () -> assertThat(pushNotificationRepository.findAll()).isEmpty(),
                () -> assertThat(pushSubscriptionRepository.findAll()).isEmpty(),
                () -> assertThat(commentRepository.findAll()).isEmpty(),
                () -> assertThat(rankingActivityRepository.findAll()).isEmpty()
        );
    }

    @DisplayName("회원을 프로필 이미지를 수정한다.")
    @Test
    void updateProfileImageByMe() {
        // given
        String expected = "https://www.abc.com/profile.jpg";
        given(imageStrategy.extractUrl(any()))
                .willReturn(expected);

        // when
        memberService.updateProfileImageByMe(조조그린_ID, new Image(MULTIPART_FILE, imageStrategy));

        // then
        assertThat(fixture.회원_조회(조조그린_ID).getPicture()).isEqualTo(expected);
    }

    @DisplayName("회원을 프로필 이미지와 닉네임, 소개를 다른 스레드에서 각각 수정하면 전체가 다 수정되어야 한다.")
    @Test
    @Rollback(value = false)
    void updateMember_multiThread() throws InterruptedException {
        // given
        ExecutorService service = Executors.newFixedThreadPool(2);
        CountDownLatch latch = new CountDownLatch(2);

        given(imageStrategy.extractUrl(any()))
                .willReturn("update-image-url");

        // when
        service.execute(() -> {
            memberService.updateByMe(조조그린_ID, "쬬그린", "HI");
            latch.countDown();
        });

        service.execute(() -> {
            memberService.updateProfileImageByMe(조조그린_ID, new Image(MULTIPART_FILE, imageStrategy));
            latch.countDown();
        });

        latch.await();

        // then
        Member result = memberService.search(조조그린_ID);
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

    @DisplayName("로그인하지 않은 멤버를 조회한다.")
    @Test
    void searchLoginMember() {
        // when
        long notLoginMemberId = 0L;
        Member actual = memberService.searchLoginMember(notLoginMemberId);

        // then
        assertThat(actual.getNickname()).isEqualTo("비회원");
    }

    @DisplayName("id들에 해당하는 멤버들을 조회한다.")
    @Test
    void searchByIdIn() {
        // given
        Member member1 = fixture.회원_조회(조조그린_ID);
        Member member2 = fixture.회원_조회(토닉_ID);
        Member member3 = fixture.회원_조회(더즈_ID);

        // when
        List<Member> actual = memberService.searchByIdIn(List.of(member1.getId(), member2.getId(), member3.getId()));

        // then
        assertThat(actual).map(Member::getId)
                .contains(member1.getId(), member2.getId(), member3.getId());
    }

    @DisplayName("이메일로 멤버를 조회한다.")
    @Test
    void findByEmail() {
        // given
        Member member = fixture.회원_조회(조조그린_ID);

        // when
        Optional<Member> actual = memberService.findByEmail(member.getEmail());

        // then
        assertAll(
                () -> assertThat(actual).isPresent(),
                () -> assertThat(actual.get().getId()).isEqualTo(member.getId())
        );
    }

    @DisplayName("멤버를 생성한다.")
    @Test
    void create() {
        // when
        Member member = memberService.create(new Member("email@email.com", "새로생긴 닉네임", "설명", "사진"));

        // then
        assertThat(memberService.findByEmail("email@email.com")).isPresent();
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(8),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(5),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(4),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(2),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(5),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(3),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(3),
                    () -> assertThat(members).map(Member::getNickname)
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
            List<Member> members = memberService.findAllByFilter(pagingParams);

            // then
            assertAll(
                    () -> assertThat(members).hasSize(2),
                    () -> assertThat(members).map(Member::getNickname)
                            .containsExactly("그랑조", "조")
            );
        }
    }
}
