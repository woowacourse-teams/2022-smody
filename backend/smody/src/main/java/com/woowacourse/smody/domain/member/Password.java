package com.woowacourse.smody.domain.member;

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
public class Password {

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{10,20}$");

    @Column(name = "password", nullable = false)
    private String value;

    public Password(String value) {
        validatePassword(value);
        this.value = value;
    }

    private void validatePassword(String value) {
        if (!PASSWORD_PATTERN.matcher(value).matches()) {
            throw new BusinessException(ExceptionData.INVALID_PASSWORD);
        }
    }

    public boolean match(String password) {
        return value.equals(password);
    }
}
