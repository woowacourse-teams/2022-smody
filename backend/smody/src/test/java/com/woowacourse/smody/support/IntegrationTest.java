package com.woowacourse.smody.support;

import com.woowacourse.smody.image.strategy.ImageStrategy;
import com.woowacourse.smody.push.service.WebPushService;
import com.woowacourse.smody.support.isoloation.SmodyTestEnvironmentExtension;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.web.client.RestTemplate;

@SpringBootTest
@ExtendWith(SmodyTestEnvironmentExtension.class)
public class IntegrationTest {

    @MockBean
    protected ImageStrategy imageStrategy;

    @MockBean
    protected WebPushService webPushService;

    @MockBean
    protected RestTemplate restTemplate;

    @Autowired
    protected ResourceFixture fixture;

    @Autowired
    private ThreadPoolTaskExecutor taskExecutor;
    
    protected void synchronize(Runnable runnable) throws InterruptedException {
        ThreadPoolExecutor threadPoolExecutor = taskExecutor.getThreadPoolExecutor();
        runnable.run();
        threadPoolExecutor.awaitTermination(500, TimeUnit.MILLISECONDS);
    }
}
