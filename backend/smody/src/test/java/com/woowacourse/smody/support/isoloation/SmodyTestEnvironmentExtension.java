package com.woowacourse.smody.support.isoloation;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class SmodyTestEnvironmentExtension implements BeforeEachCallback, AfterEachCallback {

    // TODO-테스트 클래스의 비포이치가 있다면 이거 하고 그거 중에 뭐가 먼저 발동되는가? (애프터 이치도 마찬가지)
    @Override
    public void beforeEach(ExtensionContext context) {
        SmodyDatabaseManager smodyDatabaseManager = getSmodyDatabaseManager(context);
        smodyDatabaseManager.setUp();
    }

    @Override
    public void afterEach(ExtensionContext context) {
        SmodyDatabaseManager smodyDatabaseManager = getSmodyDatabaseManager(context);
        smodyDatabaseManager.truncateTables();
    }

    private SmodyDatabaseManager getSmodyDatabaseManager(ExtensionContext context) {
        return (SmodyDatabaseManager) SpringExtension
                .getApplicationContext(context).getBean("smodyDatabaseManager");
    }
}
