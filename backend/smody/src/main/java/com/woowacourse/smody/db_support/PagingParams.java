package com.woowacourse.smody.db_support;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class PagingParams {

    private String sort;
    private Integer size;
    private Long cursorId;
    private String filter;

    public PagingParams(String sort) {
        this.sort = sort;
    }

    public PagingParams(String sort, Integer size) {
        this.sort = sort;
        this.size = size;
    }

    public PagingParams(String sort, Integer size, Long cursorId) {
        this.sort = sort;
        this.size = size;
        this.cursorId = cursorId;
    }

    public String getSort() {
        if (this.sort == null) {
            return "";
        }
        return sort;
    }

    public Integer getSize() {
        if (this.size == null || this.size <= 0) {
            return 10;
        }
        return this.size;
    }

    public Long getCursorId() {
        if (this.cursorId == null) {
            return 0L;
        }
        return this.cursorId;
    }
}
