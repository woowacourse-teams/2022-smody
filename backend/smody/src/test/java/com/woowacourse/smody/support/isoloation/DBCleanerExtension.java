package com.woowacourse.smody.support.isoloation;

import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DBCleanerExtension implements AfterEachCallback {

	@Override
	public void afterEach(ExtensionContext context) {
		DatabaseCleaner databaseCleaner = (DatabaseCleaner) SpringExtension
			.getApplicationContext(context).getBean("databaseCleaner");
		databaseCleaner.truncateTables();
	}
}
