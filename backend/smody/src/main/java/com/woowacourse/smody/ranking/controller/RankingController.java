package com.woowacourse.smody.ranking.controller;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.auth.login.LoginMember;
import com.woowacourse.smody.auth.login.RequiredLogin;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.ranking.dto.RankingActivityResponse;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.service.RankingApiService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ranking-periods")
@RequiredArgsConstructor
public class RankingController {

    private final RankingApiService rankingApiService;

    @GetMapping
    public ResponseEntity<List<RankingPeriodResponse>> findAllRankingPeriod(@ModelAttribute PagingParams pagingParams) {
        return ResponseEntity.ok(rankingApiService.findAllRankingPeriod(pagingParams));
    }

    @GetMapping("/{rankingPeriodId}/ranking-activities")
    public ResponseEntity<List<RankingActivityResponse>> findAllRankingActivity(@PathVariable Long rankingPeriodId) {
        return ResponseEntity.ok(rankingApiService.findAllRankingActivityByPeriodId(rankingPeriodId));
    }

    @GetMapping("/{rankingPeriodId}/ranking-activities/me")
    @RequiredLogin
    public ResponseEntity<RankingActivityResponse> findRankingActivityOfMine(@LoginMember TokenPayload tokenPayload,
                                                                             @PathVariable Long rankingPeriodId) {
        return ResponseEntity.ok(rankingApiService.findActivityOfMine(tokenPayload, rankingPeriodId));
    }
}
