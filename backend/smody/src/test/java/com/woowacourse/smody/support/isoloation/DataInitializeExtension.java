package com.woowacourse.smody.support.isoloation;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

public class DataInitializeExtension implements BeforeEachCallback {

	@Override
	public void beforeEach(ExtensionContext context) {
		DatabaseInitializer databaseInitializer = (DatabaseInitializer) SpringExtension
			.getApplicationContext(context).getBean("databaseInitializer");
		databaseInitializer.setUp();
	}
}
