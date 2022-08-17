package com.woowacourse.smody.domain;

import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CycleDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cycle_detail_id")
    private Long id;

    @JoinColumn(name = "cycle_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Cycle cycle;

    @OneToMany(mappedBy = "cycleDetail", cascade = {CascadeType.REMOVE}, fetch = FetchType.LAZY)
    private List<Comment> comments;

    @Column(nullable = false)
    private LocalDateTime progressTime;

    @Column(nullable = false)
    private String progressImage;

    @Column(nullable = false)
    private String description;

    public CycleDetail(Cycle cycle, LocalDateTime progressTime, String progressImage, String description) {
        validateDescription(description);
        this.cycle = cycle;
        this.progressTime = progressTime;
        this.progressImage = progressImage;
        this.description = description;
    }

    private void validateDescription(String description) {
        if (description.length() > 255 || description.isEmpty() || description.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_CYCLE_DETAIL_DESCRIPTION);
        }
    }
}
