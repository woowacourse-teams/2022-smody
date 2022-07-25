package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_member", columnNames = "email")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member {

    private static final String DEFAULT_INTRODUCTION = "스모디로 작심삼일 시작!";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String picture;

    @Column(length = 30)
    private String introduction;

    public Member(String email, String nickname, String picture, String introduction) {
        validateIntroduction(introduction);
        this.email = email;
        this.nickname = nickname;
        this.picture = picture;
        this.introduction = introduction;
    }

    public Member(String email, String nickname, String picture) {
        this(email, nickname, picture, DEFAULT_INTRODUCTION);
    }

    private void validateIntroduction(String introduction) {
        if (introduction.length() > 30) {
            throw new BusinessException(ExceptionData.INVALID_INTRODUCTION_LENGTH);
        }
    }
}
