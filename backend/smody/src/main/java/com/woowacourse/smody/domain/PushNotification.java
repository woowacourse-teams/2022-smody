package com.woowacourse.smody.domain;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class PushNotification {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "push_notification_id")
	private Long id;

	@JoinColumn(name = "member_id")
	@ManyToOne(fetch = FetchType.LAZY)
	private Member member;

	@Column(nullable = false)
	private String message;

	@Column(nullable = false)
	private LocalDateTime pushTime;

	@Enumerated(EnumType.STRING)
	private PushStatus pushStatus;

	public PushNotification(String message, LocalDateTime pushTime, PushStatus pushStatus, Member member) {
		this.message = message;
		this.pushTime = pushTime;
		this.pushStatus = pushStatus;
		this.member = member;
	}
}
