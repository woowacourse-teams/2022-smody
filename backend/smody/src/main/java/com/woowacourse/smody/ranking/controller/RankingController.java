package com.woowacourse.smody.ranking.controller;

import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.service.RankingService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking-periods")
@RequiredArgsConstructor
public class RankingController {

    private final RankingService rankingService;

    @GetMapping
    public ResponseEntity<List<RankingPeriodResponse>> findAll() {
        return ResponseEntity.ok(rankingService.findAllPeriodLatest());
    }

    @GetMapping("/{rankingPeriodId}/ranking-activities")
    public ResponseEntity<List<RankingActivityResponse>> findAllActivity(@PathVariable Long rankingPeriodId) {
        return ResponseEntity.ok(rankingService.findAllActivity(rankingPeriodId));
    }
}
