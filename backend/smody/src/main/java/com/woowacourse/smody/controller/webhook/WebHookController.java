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

    public static final String GITHUB_HOOK_ID = "369627662";
    public static final String DEPLOY_BRANCH = "CD";

    @PostMapping("/deploy")
    public ResponseEntity<Void> deploy(@RequestHeader(value = "X-GitHub-Hook-ID") String gitHubHookId,
                                       @RequestBody GitHubHookRequest gitHubHookRequest) {
        if (gitHubHookId.equals(GITHUB_HOOK_ID) && gitHubHookRequest.parseBranch().equals(DEPLOY_BRANCH)) {
            try {
                Runtime.getRuntime().exec("/home/ubuntu/spring-deploy.sh");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }

    @GetMapping("isReal")
    public String isReal() {
        return "머지 갑니다.";
    }
}
