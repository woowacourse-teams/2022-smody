package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.ChallengeResponse;
import com.woowacourse.smody.dto.SuccessChallengeResponse;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.ChallengeService;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    @GetMapping
    public ResponseEntity<List<ChallengeResponse>> findAllWithChallengerCount(Pageable pageable) {
        return ResponseEntity.ok(challengeService.findAllWithChallengerCount(LocalDateTime.now(), pageable));
    }

    @GetMapping("/auth")
    public ResponseEntity<List<ChallengeResponse>> findAllWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                                      Pageable pageable) {
        return ResponseEntity.ok(challengeService.findAllWithChallengerCount(tokenPayload, LocalDateTime.now(), pageable));
    }

    @GetMapping(value = "/me")
    public ResponseEntity<List<SuccessChallengeResponse>> searchSuccessOfMine(@LoginMember TokenPayload tokenPayload,
                                                                                 Pageable pageable) {
        return ResponseEntity.ok(challengeService.searchSuccessOfMine(tokenPayload, pageable));
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ChallengeResponse> findOneWithChallengerCount(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findOneWithChallengerCount(LocalDateTime.now(), id));
    }

    @GetMapping(value = "/{id}/auth")
    public ResponseEntity<ChallengeResponse> findOneWithChallengerCount(@LoginMember TokenPayload tokenPayload,
                                                                        @PathVariable Long id) {
        return ResponseEntity.ok(challengeService.findOneWithChallengerCount(tokenPayload, LocalDateTime.now(), id));
    }
}
