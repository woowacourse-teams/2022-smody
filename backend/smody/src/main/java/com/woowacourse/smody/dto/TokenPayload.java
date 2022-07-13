package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.Member;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenPayload {

    private Long id;

    public TokenPayload(Member member) {
        this.id = member.getId();
    }

    public Map<String, Object> toMap() {
        return Map.of("id", id);
    }
}
