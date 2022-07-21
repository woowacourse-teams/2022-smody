package com.woowacourse.smody.webhook;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/web-hook")
public class WebHookController {

    @Value("${web-hook.github-hook-id}")
    private String githubHookId;
    @Value("${web-hook.deploy-branch}")
    private String deployBranch;
    @Value("${web-hook.deploy-script-directory}")
    private String deployScriptDirectory;

    @PostMapping("/deploy")
    public ResponseEntity<Void> deploy(@RequestHeader(value = "X-GitHub-Hook-ID") String gitHubHookId,
                                       @RequestBody GitHubHookRequest gitHubHookRequest) {
        if (gitHubHookId.equals(githubHookId) && gitHubHookRequest.parseBranch().equals(deployBranch)) {
            try {
                Runtime.getRuntime().exec(deployScriptDirectory);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.noContent().build();
    }
}
