package com.woowacourse.smody.controller.webhook;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-hook")
public class WebHookController {

    @PostMapping("/deploy")
    public ResponseEntity<Void> deploy(@RequestHeader(value = "X-GitHub-Hook-ID") String gitHubHookId,
                                       @RequestBody GitHubHookRequest gitHubHookRequest) {
        if (gitHubHookId.equals("369627662") && gitHubHookRequest.parseBranch().equals("CD")) {
            try {
                Runtime.getRuntime().exec("/home/ubuntu/spring-deploy.sh");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/isReal")
    public String isReal() {
        return "노훕아 어딧니, 되야함, 마지막 테스트";
    }
}
