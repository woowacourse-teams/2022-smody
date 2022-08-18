package com.woowacourse.smody.member.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import com.woowacourse.smody.image.domain.Image;
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
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(uniqueConstraints = {@UniqueConstraint(name = "unique_column_in_member", columnNames = "email")})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
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

    @Column(length = 30)
    private String introduction;

    @Column(nullable = false)
    private String picture;

    public Member(String email, String nickname, String introduction, String picture) {
        validateIntroduction(introduction);
        this.email = email;
        this.nickname = nickname;
        this.introduction = introduction;
        this.picture = picture;
    }

    public Member(String email, String nickname, String picture) {
        this(email, nickname, DEFAULT_INTRODUCTION, picture);
    }

    private void validateIntroduction(String introduction) {
        if (introduction.length() > 30) {
            throw new BusinessException(ExceptionData.INVALID_INTRODUCTION_LENGTH);
        }
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updatePicture(Image picture) {
        this.picture = picture.getUrl();
    }

    public void updateIntroduction(String introduction) {
        this.introduction = introduction;
    }
}
