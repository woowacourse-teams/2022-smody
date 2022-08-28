package com.woowacourse.smody.support.isoloation;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DatabaseCleaner {

	private final EntityManager entityManager;
	private final List<String> tableNames;

	public DatabaseCleaner(EntityManager entityManager) {
		this.entityManager = entityManager;
		this.tableNames = extractTableNames(entityManager);
	}

	private List<String> extractTableNames(EntityManager entityManager) {
		return entityManager.getMetamodel().getEntities().stream()
			.filter(entityType -> entityType.getJavaType().getAnnotation(Entity.class) != null)
			.map(this::convertCamelToUnderscore)
			.collect(Collectors.toList());
	}

	private String convertCamelToUnderscore(EntityType<?> entityType) {
		String regex = "([a-z])([A-Z]+)";
		String replacement = "$1_$2";
		return entityType.getName()
			.replaceAll(regex, replacement)
			.toUpperCase();
	}

	@Transactional
	public void truncateTables() {
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
		for (String tableName : tableNames) {
			entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
			entityManager.createNativeQuery(
				"ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_ID" +  " RESTART WITH 1"
			).executeUpdate();
		}
		entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
	}
}
