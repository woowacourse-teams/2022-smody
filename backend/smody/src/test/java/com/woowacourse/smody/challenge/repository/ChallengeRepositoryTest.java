package com.woowacourse.smody.challenge.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.support.RepositoryTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class ChallengeRepositoryTest extends RepositoryTest {

    @Autowired
    private ChallengeRepository challengeRepository;

    @DisplayName("임의의 챌린지 데이터 2개를 조회한다.")
    @Test
    void findAllOrderByRandomLimit2() {
        // given
        PagingParams pagingParams = new PagingParams("random", 2);

        // when
        List<Challenge> actual = challengeRepository.findAllByFilter(pagingParams);

        // then
        assertThat(actual).hasSize(2);
    }

}
