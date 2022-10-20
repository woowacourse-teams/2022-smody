package com.woowacourse.smody.auth.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@EqualsAndHashCode
public class TokenPayload {

    public static final TokenPayload NOT_LOGIN_TOKEN_PAYLOAD = new TokenPayload(0L);

    private Long id;

    public Map<String, Object> toMap() {
        return Map.of("id", id);
    }
}
