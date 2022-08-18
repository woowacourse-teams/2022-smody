package com.woowacourse.smody.auth.dto;

import com.woowacourse.smody.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.json.JSONObject;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class LoginRequest {

    private String email;
    private String name;
    private String picture;

    public LoginRequest(JSONObject jsonObject) {
        this.email = jsonObject.getString("email");
        this.name = jsonObject.getString("name");
        this.picture = jsonObject.getString("picture");
    }

    public LoginRequest(Member member) {
        this.email = member.getEmail();
        this.name = member.getNickname();
        this.picture = member.getPicture();
    }

    public Member toMember() {
        return new Member(email, name, picture);
    }
}
