package com.woowacourse.smody.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.push.service.WebPushService;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironment;

@SmodyTestEnvironment
public class IntegrationTest {

    @MockBean
    protected ImageStrategy imageStrategy;

    @MockBean
    protected WebPushService webPushService;

    @Autowired
    protected ResourceFixture fixture;
}
