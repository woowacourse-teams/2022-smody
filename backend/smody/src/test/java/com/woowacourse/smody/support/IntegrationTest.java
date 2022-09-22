package com.woowacourse.smody.support;

import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.push.service.WebPushService;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest
@ExtendWith(SmodyTestEnvironmentExtension.class)
public class IntegrationTest {

    @MockBean
    protected ImageStrategy imageStrategy;

    @MockBean
    protected WebPushService webPushService;

    @Autowired
    protected ResourceFixture fixture;
}
