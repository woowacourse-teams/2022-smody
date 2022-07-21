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
        if (!gitHubHookId.equals("369627662") || !gitHubHookRequest.getRef().equals("CD")) {
            return ResponseEntity.noContent().build();
        }

        try {
            Runtime.getRuntime().exec("/home/ubuntu/spring-deploy.sh");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/isReal")
    public String isReal() {
        return "이게 된다고? 진짜? 레알? 이거맞음???? 우석아 안녕, 나 이거 우리집에서 푸쉬함 ㅎ, 이제 우리 웹훅아니면 배포 안됨";
    }
}
