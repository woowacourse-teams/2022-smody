package com.woowacourse.smody.support;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.challenge.repository.ChallengeRepository;
import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.comment.repository.CommentRepository;
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.cycle.repository.CycleRepository;
import com.woowacourse.smody.image.domain.Image;
import com.woowacourse.smody.member.domain.Member;
import com.woowacourse.smody.member.repository.MemberRepository;
import com.woowacourse.smody.push.domain.PushCase;
import com.woowacourse.smody.push.domain.PushNotification;
import com.woowacourse.smody.push.domain.PushStatus;
import com.woowacourse.smody.push.domain.PushSubscription;
import com.woowacourse.smody.push.repository.PushNotificationRepository;
import com.woowacourse.smody.push.repository.PushSubscriptionRepository;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Component
@SuppressWarnings("NonAsciiCharacters")
public class ResourceFixture {

	private final MemberRepository memberRepository;
	private final ChallengeRepository challengeRepository;
	private final CycleRepository cycleRepository;
	private final PushNotificationRepository pushNotificationRepository;
	private final PushSubscriptionRepository pushSubscriptionRepository;
	private final CommentRepository commentRepository;

	public ResourceFixture(MemberRepository memberRepository,
		ChallengeRepository challengeRepository,
		CycleRepository cycleRepository,
		PushNotificationRepository pushNotificationRepository,
		PushSubscriptionRepository pushSubscriptionRepository,
		CommentRepository commentRepository) {
		this.memberRepository = memberRepository;
		this.challengeRepository = challengeRepository;
		this.cycleRepository = cycleRepository;
		this.pushNotificationRepository = pushNotificationRepository;
		this.pushSubscriptionRepository = pushSubscriptionRepository;
		this.commentRepository = commentRepository;
	}

    public static final Long 조조그린_ID = 1L;
    public static final Long 더즈_ID = 2L;
    public static final Long 토닉_ID = 3L;
    public static final Long 알파_ID = 4L;

    public static final Long 스모디_방문하기_ID = 1L;
    public static final Long 미라클_모닝_ID = 2L;
    public static final Long 오늘의_운동_ID = 3L;
    public static final Long 알고리즘_풀기_ID = 4L;
    public static final Long JPA_공부_ID = 5L;

	public static final MultipartFile MULTIPART_FILE = new MockMultipartFile(
		"progressImage", "progressImage.jpg", "image/jpg", "image".getBytes()
	);
	public static final Image 이미지 = new Image(MULTIPART_FILE, image -> "image.jpg");

	public static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public Member 회원_조회(Long id) {
        return memberRepository.findById(id).orElseThrow();
    }

    public Challenge 챌린지_조회(Long id) {
        return challengeRepository.findById(id).orElseThrow();
    }

    public Cycle 사이클_생성(Long memberId, Long challengeId, Progress progress, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return 인증_기록_추가(cycle, progress);
    }

    public Cycle 사이클_생성_NOTHING(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return 인증_기록_추가(cycle, Progress.NOTHING);
    }

    public Cycle 사이클_생성_FIRST(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return 인증_기록_추가(cycle, Progress.FIRST);
    }

    public Cycle 사이클_생성_SECOND(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return 인증_기록_추가(cycle, Progress.SECOND);
    }

    public Cycle 사이클_생성_SUCCESS(Long memberId, Long challengeId, LocalDateTime startTime) {
        Cycle cycle = new Cycle(회원_조회(memberId), 챌린지_조회(challengeId), Progress.NOTHING, startTime);
        return 인증_기록_추가(cycle, Progress.SUCCESS);
    }

    private Cycle 인증_기록_추가(Cycle cycle, Progress progress) {
        List<CycleDetail> cycleDetails = List.of(
                new CycleDetail(
                        cycle, cycle.getStartTime().plusSeconds(1L), "image1.jpg", "first"
                ),
                new CycleDetail(
                        cycle, cycle.getStartTime().plusDays(1L), "image2.jpg", "second"
                ),
                new CycleDetail(
                        cycle, cycle.getStartTime().plusDays(2L), "image3.jpg", "third"
                )
        );
        for (int i = 0; i < progress.getCount(); i++) {
            cycle.increaseProgress(cycleDetails.get(i).getProgressTime(), 이미지, cycleDetails.get(i).getDescription());
        }
        return cycleRepository.save(cycle);
    }

	public PushNotification 발송된_알림_생성(Long memberId, Long pathId, LocalDateTime pushTime, PushCase pushCase) {
		PushNotification notification = PushNotification.builder()
			.member(회원_조회(memberId))
			.pathId(pathId)
			.pushTime(pushTime)
			.pushStatus(PushStatus.COMPLETE)
			.pushCase(pushCase)
			.message("알림 전송")
			.build();
		return pushNotificationRepository.save(notification);
	}

	public PushNotification 발송_예정_알림_생성(Long memberId, Long pathId, LocalDateTime pushTime, PushCase pushCase) {
		PushNotification notification = PushNotification.builder()
			.member(회원_조회(memberId))
			.pathId(pathId)
			.pushTime(pushTime)
			.pushStatus(PushStatus.IN_COMPLETE)
			.pushCase(pushCase)
			.message("알림 전송")
			.build();
		return pushNotificationRepository.save(notification);
	}

	public PushSubscription 알림_구독(Long memberId, String endpoint) {
		return pushSubscriptionRepository.save(new PushSubscription(
			endpoint,
			"p256dh",
			"auth",
			회원_조회(memberId)
		));
	}

	public Comment 댓글_등록(CycleDetail cycleDetail, Long memberId, String content) {
		Comment comment = new Comment(cycleDetail, 회원_조회(memberId), content);
		return commentRepository.save(comment);
	}

	@Transactional
	public void 사이클_인증(Long cycleId, LocalDateTime progressTime) {
		Cycle cycle = cycleRepository.findById(cycleId).orElseThrow();
		cycle.increaseProgress(progressTime, 이미지, "description");
	}

	public Member 회원_추가(String nickName, String email) {
		String picture = 이미지.getUrl();
		String introduction = "자기소개";
		Member member = new Member(email, nickName, picture, introduction);
		return memberRepository.save(member);
	}
}
