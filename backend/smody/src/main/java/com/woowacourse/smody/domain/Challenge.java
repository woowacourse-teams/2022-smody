package com.woowacourse.smody.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.woowacourse.smody.dto.ChallengeRequest;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_challenge", columnNames = "name")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Challenge {

    private static final String DEFAULT_INTRODUCTION = " 입니다";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "challenge_id")
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String emoji;

    @Column(nullable = false)
    private String description;

    public Challenge(String name) {
        validateDescription(name + DEFAULT_INTRODUCTION);
        validateName(name);
        this.name = name;
    }

    public Challenge(String name, String description, String emoji) {
        validateDescription(description);
        validateName(name);
        this.name = name;
        this.description = description;
        this.emoji = emoji;
    }

    private void validateDescription(String description) {
        if (description.length() > 255 || description.isEmpty() || description.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_CHALLENGE_DESCRIPTION);
        }
    }

    private void validateName(String name) {
        if (name.length() > 30 || name.isEmpty() || name.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_CHALLENGE_NAME);
        }
    }
}
