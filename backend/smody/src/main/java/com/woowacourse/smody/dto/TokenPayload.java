package com.woowacourse.smody.dto;

import com.woowacourse.smody.domain.member.Member;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class TokenPayload {

    private Long id;
    private String nickname;

    public TokenPayload(Member member) {
        this.id = member.getId();
        this.nickname = member.getNickname().getValue();
    }

    public Map<String, Object> toMap() {
        return Map.of("id", id, "nickname", nickname);
    }
}
