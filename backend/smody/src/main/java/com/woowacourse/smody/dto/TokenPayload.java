package com.woowacourse.smody.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenPayload {

    private Long id;

    public Map<String, Object> toMap() {
        return Map.of("id", id);
    }
}
