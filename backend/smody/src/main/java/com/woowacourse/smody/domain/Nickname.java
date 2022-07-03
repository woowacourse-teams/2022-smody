package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.util.regex.Pattern;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Nickname {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^\\S{2,10}$");

    @Column(name = "nickname")
    private String value;

    public Nickname(String value) {
        validateNickname(value);
        this.value = value;
    }

    private void validateNickname(String value) {
        if (!NICKNAME_PATTERN.matcher(value).matches()) {
            throw new BusinessException(ExceptionData.INVALID_NICKNAME);
        }
    }
}
