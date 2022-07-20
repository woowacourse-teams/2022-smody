package com.woowacourse.smody.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-hook")
public class WebHookController {

    @PostMapping("/deploy")
    public ResponseEntity<String> deploy(@RequestBody String body) {
        try {
            Runtime.getRuntime().exec("/home/ubuntu/spring-deploy.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/isReal")
    public String isReal() {
        return "이게 된다고? 진짜? 레알? 이거 맞음???? 우석아 안녕";
    }
}
