package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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
    private Integer emojiIndex;

    @Column(nullable = false)
    private Integer colorIndex;

    @Column(nullable = false)
    private String description;

    public Challenge(String name) {
        validateDescription(name + DEFAULT_INTRODUCTION);
        validateName(name);
        this.name = name;
    }

    public Challenge(String name, String description, Integer emojiIndex, Integer colorIndex) {
        validateDescription(description);
        validateName(name);
        this.name = name;
        this.description = description;
        this.emojiIndex = emojiIndex;
        this.colorIndex = colorIndex;
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
