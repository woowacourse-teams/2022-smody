package com.woowacourse.smody.controller;

import com.woowacourse.smody.auth.LoginMember;
import com.woowacourse.smody.dto.CycleRequest;
import com.woowacourse.smody.dto.TokenPayload;
import com.woowacourse.smody.service.CycleService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CycleController {

    private final CycleService cycleService;

    @PostMapping("/cycles")
    public ResponseEntity<Void> create(@LoginMember TokenPayload tokenPayload,
                                       @RequestBody CycleRequest cycleRequest) {
        Long cycleId = cycleService.create(cycleRequest);
        return ResponseEntity.created(URI.create("/cycles/" + cycleId)).build();
    }
}
