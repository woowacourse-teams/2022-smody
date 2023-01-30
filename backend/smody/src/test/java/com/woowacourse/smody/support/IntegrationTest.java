package com.woowacourse.smody.support;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.web.client.RestTemplate;

import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.push.api.WebPushApi;
import com.woowacourse.smody.support.config.AsyncTestConfig;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;

@SpringBootTest
@ExtendWith(SmodyTestEnvironmentExtension.class)
@Import(AsyncTestConfig.class)
public class IntegrationTest {

    @MockBean
    protected ImageStrategy imageStrategy;

    @MockBean
    protected WebPushApi webPushApi;

    @MockBean
    protected RestTemplate restTemplate;

    @Autowired
    protected ResourceFixture fixture;
}
