package com.woowacourse.smody.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import netscape.javascript.JSObject;
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
}
