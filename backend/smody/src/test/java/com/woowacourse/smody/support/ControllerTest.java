package com.woowacourse.smody.support;

import static org.springframework.restdocs.operation.preprocess.Preprocessors.modifyUris;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.woowacourse.smody.auth.api.GoogleApi;
import com.woowacourse.smody.auth.controller.OauthController;
import com.woowacourse.smody.auth.service.OauthService;
import com.woowacourse.smody.auth.token.JwtTokenExtractor;
import com.woowacourse.smody.auth.token.JwtTokenProvider;
import com.woowacourse.smody.challenge.controller.ChallengeController;
import com.woowacourse.smody.challenge.service.ChallengeQueryService;
import com.woowacourse.smody.challenge.service.ChallengeService;
import com.woowacourse.smody.comment.controller.CommentController;
import com.woowacourse.smody.comment.service.CommentQueryService;
import com.woowacourse.smody.comment.service.CommentService;
import com.woowacourse.smody.cycle.controller.CycleController;
import com.woowacourse.smody.cycle.service.CycleQueryService;
import com.woowacourse.smody.cycle.service.CycleService;
import com.woowacourse.smody.feed.controller.FeedController;
import com.woowacourse.smody.feed.service.FeedQueryService;
import com.woowacourse.smody.member.controller.MemberController;
import com.woowacourse.smody.member.service.MemberService;
import com.woowacourse.smody.push.controller.PushNotificationController;
import com.woowacourse.smody.push.controller.PushSubscriptionController;
import com.woowacourse.smody.push.service.PushNotificationService;
import com.woowacourse.smody.push.service.PushSubscriptionService;
import com.woowacourse.smody.push.service.WebPushService;
import com.woowacourse.smody.ranking.controller.RankingPeriodController;
import com.woowacourse.smody.ranking.service.RankingPeriodService;
import com.woowacourse.smody.ui.admin.controller.AdminSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = {
        MemberController.class,
        CycleController.class,
        ChallengeController.class,
        OauthController.class,
        CommentController.class,
        FeedController.class,
        PushSubscriptionController.class,
        PushNotificationController.class,
        RankingPeriodController.class
})
@Import({JwtTokenProvider.class, JwtTokenExtractor.class, AdminSecurityConfig.class, SecurityTestConfig.class})
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureRestDocs
public class ControllerTest {

    protected static final OperationRequestPreprocessor HOST_INFO = preprocessRequest(modifyUris()
            .scheme("https")
            .host("www.smody.co.kr")
            .removePort(), prettyPrint()
    );

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    protected JwtTokenProvider jwtTokenProvider;

    @MockBean
    protected MemberService memberService;

    @MockBean
    protected CycleService cycleService;

    @MockBean
    protected ChallengeService challengeService;

    @MockBean
    protected ChallengeQueryService challengeQueryService;

    @MockBean
    protected CycleQueryService cycleQueryService;

    @MockBean
    protected FeedQueryService feedQueryService;

    @MockBean
    protected CommentQueryService commentQueryService;

    @MockBean
    protected CommentService commentService;

    @MockBean
    protected GoogleApi googleApi;

    @MockBean
    protected OauthService oauthService;

    @MockBean
    protected PushSubscriptionService pushSubscriptionService;

    @MockBean
    protected WebPushService webPushService;

    @MockBean
    protected PushNotificationService pushNotificationService;

    @MockBean
    protected RankingPeriodService rankingPeriodService;
}
