package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenPayload {

    private Long id;

    public Map<String, Object> toMap() {
        return Map.of("id", id);
    }
}
