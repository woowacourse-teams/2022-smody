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
        System.out.println("### gitHubHookId = " + gitHubHookId);
        System.out.println("### gitHubHookRequest = " + gitHubHookRequest.getRef());
        System.out.println("### gitHubHookRequest = " + gitHubHookRequest.parseBranch());

        if (!gitHubHookId.equals("369627662") || !gitHubHookRequest.parseBranch().equals("CD")) {
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
        return "다시 테스트, 이거 나오면 성공";
    }
}
