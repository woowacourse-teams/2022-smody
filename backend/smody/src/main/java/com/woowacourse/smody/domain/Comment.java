package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
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
public class Comment extends AuditingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @JoinColumn(name = "cycle_detail_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private CycleDetail cycleDetail;

    @JoinColumn(name = "member_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Column(nullable = false)
    private String context;

    public Comment(CycleDetail cycleDetail, Member member, String context) {
        validateContext(context);
        this.cycleDetail = cycleDetail;
        this.member = member;
        this.context = context;
    }

    private void validateContext(String context) {
        if (context.isBlank() || context.length() > 255) {
            throw new BusinessException(ExceptionData.INVALID_COMMENT_CONTENT);
        }
    }
}
