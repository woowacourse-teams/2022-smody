package com.woowacourse.smody.support.isoloation;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class SmodyTestEnvironmentExtension implements BeforeEachCallback, AfterEachCallback {

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
		return (SmodyDatabaseManager)SpringExtension
			.getApplicationContext(context).getBean("smodyDatabaseManager");
	}
}
