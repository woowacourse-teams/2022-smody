package com.woowacourse.smody.ranking.domain;

import com.woowacourse.smody.member.domain.Member;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class RankingActivity {

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

    public boolean isDraw(RankingActivity other) {
        return this.point.equals(other.getPoint());
    }
}
