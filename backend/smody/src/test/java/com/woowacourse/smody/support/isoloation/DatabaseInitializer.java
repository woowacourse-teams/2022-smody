package com.woowacourse.smody.support.isoloation;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.member.domain.Member;

@Component
public class DatabaseInitializer {

	private final EntityManager entityManager;

	public DatabaseInitializer(EntityManager entityManager) {
		this.entityManager = entityManager;
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
			0,
			1
		));
		entityManager.persist(new Challenge(
			"미라클 모닝",
			"미라클 모닝 챌린지입니다",
			0,
			1
		));
		entityManager.persist(new Challenge(
			"오늘의 운동",
			"오늘의 운동 챌린지입니다",
			0,
			1
		));
		entityManager.persist(new Challenge(
			"알고리즘 풀기",
			"알고리즘 풀기 챌린지입니다",
			0,
			1
		));
		entityManager.persist(new Challenge(
			"JPA 공부",
			"JPA 공부 챌린지입니다",
			0,
			1
		));
	}
}
