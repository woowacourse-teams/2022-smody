package com.woowacourse.smody.support;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.woowacourse.smody.support.isoloation.DatabaseCleaner;
import com.woowacourse.smody.support.isoloation.DatabaseInitializer;

@DataJpaTest(showSql = false)
@Import({ResourceFixture.class, DatabaseInitializer.class, DatabaseCleaner.class})
public class RepositoryTest {

	@Autowired
	private DatabaseInitializer databaseInitializer;

	@Autowired
	private DatabaseCleaner databaseCleaner;

	@BeforeEach
	void init() {
		databaseInitializer.setUp();
	}

	@AfterEach
	void clear() {
		databaseCleaner.truncateTables();
	}
}
