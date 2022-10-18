package com.woowacourse.smody.support.isoloation;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.member.domain.Member;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.metamodel.EntityType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class SmodyDatabaseManager {

    private static final Logger log = LoggerFactory.getLogger(SmodyDatabaseManager.class);

    private final EntityManager entityManager;
    private final List<String> tableNames;

    public SmodyDatabaseManager(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.tableNames = extractTableNames(entityManager);
    }

    private List<String> extractTableNames(EntityManager entityManager) {
        return entityManager.getMetamodel().getEntities().stream()
                .filter(this::isEntity)
                .map(this::convertCamelToSnake)
                .collect(Collectors.toList());
    }

    private boolean isEntity(EntityType<?> entityType) {
        return entityType.getJavaType().getAnnotation(Entity.class) != null;
    }

    private String convertCamelToSnake(EntityType<?> entityType) {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return entityType.getName()
                .replaceAll(regex, replacement)
                .toUpperCase();
    }

    @Transactional
    public void truncateTables() {
        log.info("------------------------------------ 테스트 종료 ------------------------------------");
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY FALSE").executeUpdate();
        for (String tableName : tableNames) {
            entityManager.createNativeQuery("TRUNCATE TABLE " + tableName).executeUpdate();
            entityManager.createNativeQuery(
                    "ALTER TABLE " + tableName + " ALTER COLUMN " + tableName + "_ID" + " RESTART WITH 1"
            ).executeUpdate();
        }
        entityManager.createNativeQuery("SET REFERENTIAL_INTEGRITY TRUE").executeUpdate();
    }

    @Transactional
    public void setUp() {
        entityManager.persist(new Member(
                "iawbg13@gmail.com",
                "조조그린",
                "https://lh3.googleusercontent.com/a-/AFdZucp2Jil0TsQ_Edr7hFi7RGfyJK48yeffjHgCVI3JNw=s96-c"
        ));
        entityManager.persist(new Member(
                "ldk980130@gmail.com",
                "더즈",
                "https://lh3.googleusercontent.com/a-/AFdZuco9OnuGCM1lskeFuuYNyHXTZ7YVZTw5L1kOfGWapQ=s96-c-rg-br100"
        ));
        entityManager.persist(new Member(
                "diddmlvkf@gmail.com",
                "토닉",
                "https://lh3.googleusercontent.com/a/AItbvmm1hUW1WalreKj7mQ54AJeTx7xTpkLHQ91Dc6BC=s576-p-rw-no-mo"
        ));
        entityManager.persist(new Member(
                "bcc101106@gmail.com",
                "알파",
                "https://lh3.googleusercontent.com/a/AItbvmmloBfQkCwgxHvdDFPtuqIkAbMKWjk_NHd7xXjq=s192-c-br100-rg-mo"
        ));

        entityManager.persist(new Challenge(
                "스모디 방문하기",
                "스모디 방문하기 챌린지입니다",
                1,
                1
        ));
        entityManager.persist(new Challenge(
                "미라클 모닝",
                "미라클 모닝 챌린지입니다",
                1,
                1
        ));
        entityManager.persist(new Challenge(
                "오늘의 운동",
                "오늘의 운동 챌린지입니다",
                1,
                1
        ));
        entityManager.persist(new Challenge(
                "알고리즘 풀기",
                "알고리즘 풀기 챌린지입니다",
                1,
                1
        ));
        entityManager.persist(new Challenge(
                "JPA 공부",
                "JPA 공부 챌린지입니다",
                1,
                1
        ));
        log.info("------------------------------------ 테스트 시작 ------------------------------------");
    }
}
