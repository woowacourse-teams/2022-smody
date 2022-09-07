package com.woowacourse.smody.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PagingParams {

    private String sort;
    private Integer size;
    private Long cursorId;
    private String filter;

    public PagingParams(String sort, int size) {
        this.sort = sort;
        this.size = size;
    }

    public PagingParams(String sort, int size, Long cursorId) {
        this.sort = sort;
        this.size = size;
        this.cursorId = cursorId;
    }

    public Integer getDefaultSize() {
        if (this.size == null || this.size <= 0) {
            return 10;
        }
        return this.size;
    }

    public Long getDefaultCursorId() {
        if (this.cursorId == null) {
            return 0L;
        }
        return this.cursorId;
    }
}