package com.woowacourse.smody.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.woowacourse.smody.ranking.domain.Duration;
import com.woowacourse.smody.ranking.domain.RankingPeriod;
import com.woowacourse.smody.ranking.dto.RankingPeriodResponse;
import com.woowacourse.smody.ranking.repository.RankingPeriodRepository;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class RankingAcceptanceTest extends AcceptanceTest {

    @Autowired
    private RankingPeriodRepository rankingPeriodRepository;

    @DisplayName("랭킹 기간 전체 조회")
    @Test
    void getRankingPeriods() {
        // given
        rankingPeriodRepository.save(
                new RankingPeriod(LocalDateTime.now(), Duration.WEEK)
        );

        // when
        ExtractableResponse<Response> response = RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .queryParams(Map.of(
                        "size", 10,
                        "sort", "latest"
                ))
                .when()
                .get("/ranking-periods?sort=startDate:desc")
                .then().log().all()
                .extract();
        List<RankingPeriodResponse> rankingPeriodResponses = response.body()
                .jsonPath()
                .getList("", RankingPeriodResponse.class);

        // then
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(rankingPeriodResponses).hasSize(1)
        );
    }
}
