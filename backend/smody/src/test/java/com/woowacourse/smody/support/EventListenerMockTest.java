package com.woowacourse.smody.support;

import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowacourse.smody.push.event.ChallengePushEventListener;
import com.woowacourse.smody.push.event.CommentPushEventListener;
import com.woowacourse.smody.push.event.SubscriptionPushEventListener;
import com.woowacourse.smody.ranking.event.RankingPointEventListener;

public class EventListenerMockTest extends IntegrationTest {

    @MockBean
    protected ChallengePushEventListener challengePushStrategy;

    @MockBean
    protected SubscriptionPushEventListener subscriptionPushStrategy;

    @MockBean
    protected CommentPushEventListener commentPushStrategy;

    @MockBean
    protected RankingPointEventListener rankingPointEventListener;
}
