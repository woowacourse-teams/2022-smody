package com.woowacourse.smody.cycle.domain;

import com.woowacourse.smody.comment.domain.Comment;
import com.woowacourse.smody.exception.BusinessException;
import com.woowacourse.smody.exception.ExceptionData;
import java.time.LocalDateTime;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

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

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Progress progress;

    public CycleDetail(Cycle cycle, LocalDateTime progressTime, String progressImage,
                       String description, Progress progress) {
        validateDescription(description);
        this.cycle = cycle;
        this.progressTime = progressTime;
        this.progressImage = progressImage;
        this.description = description;
        this.progress = progress;
    }

    public CycleDetail(Cycle cycle, LocalDateTime progressTime, String progressImage, String description) {
        this(cycle, progressTime, progressImage, description, Progress.NOTHING);
    }

    private void validateDescription(String description) {
        if (description.length() > 255 || description.isEmpty() || description.isBlank()) {
            throw new BusinessException(ExceptionData.INVALID_CYCLE_DETAIL_DESCRIPTION);
        }
    }

    public void setProgress(Progress progress) {
        this.progress = progress;
    }
}
