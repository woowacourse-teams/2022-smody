package com.woowacourse.smody.support;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.woowacourse.smody.support.isoloation.SmodyDatabaseManager;

@DataJpaTest(showSql = false)
@Import({ResourceFixture.class, SmodyDatabaseManager.class})
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
