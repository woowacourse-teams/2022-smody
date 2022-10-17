package com.woowacourse.smody.ranking.domain;

import com.woowacourse.smody.cycle.domain.Progress;
import com.woowacourse.smody.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_column_in_ranking_activity",
                columnNames = {"member_id", "ranking_period_id"} // TODO-유니크 조건 2개 엮은 이유는?
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankingActivity {

    private static final int DEFAULT_POINT = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ranking_activity_id")
    private Long id;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @JoinColumn(name = "ranking_period_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private RankingPeriod rankingPeriod;

    @Column(nullable = false)
    private Integer point;

    public RankingActivity(Member member, RankingPeriod rankingPeriod, Integer point) {
        this.member = member;
        this.rankingPeriod = rankingPeriod;
        this.point = point;
    }

    public static RankingActivity ofZeroPoint(Member member, RankingPeriod period) {
        return new RankingActivity(member, period, DEFAULT_POINT);
    }

    public void doActivity(Progress progress) {
        this.point += Point.calculate(progress);
    }
}
