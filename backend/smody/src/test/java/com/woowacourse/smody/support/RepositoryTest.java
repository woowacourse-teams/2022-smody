package com.woowacourse.smody.support;

import com.woowacourse.smody.cycle.repository.DynamicCycleRepository;
import com.woowacourse.smody.cycle.repository.DynamicCycleRepositoryImpl;
import com.woowacourse.smody.support.config.QueryFactoryTestConfig;
import com.woowacourse.smody.support.isoloation.SmodyDatabaseManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest(showSql = false)
@Import({
        ResourceFixture.class,
        SmodyDatabaseManager.class,
        QueryFactoryTestConfig.class,
        DynamicCycleRepositoryImpl.class
})
public class RepositoryTest {

    @Autowired
    private SmodyDatabaseManager smodyDatabaseManager;

    @BeforeEach
    void init() {
        smodyDatabaseManager.setUp();
    }

    @AfterEach
    void clear() {
        smodyDatabaseManager.truncateTables();
    }
}
