package com.woowacourse.smody.ranking.controller;

import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.service.RankingPeriodService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking-periods")
@RequiredArgsConstructor
public class RankingPeriodController {

    private final RankingPeriodService rankingPeriodService;

    @GetMapping
    public ResponseEntity<List<RankingPeriodResponse>> findAll() {
        List<RankingPeriodResponse> rankingPeriodResponses = rankingPeriodService.findAll();
        return ResponseEntity.ok(rankingPeriodResponses);
    }
}
