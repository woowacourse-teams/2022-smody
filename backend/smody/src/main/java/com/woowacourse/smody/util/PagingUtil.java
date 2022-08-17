package com.woowacourse.smody.util;

import org.springframework.data.domain.Pageable;

import java.util.Collections;
import java.util.List;

public class PagingUtil {
    private PagingUtil() {
        // static 용도이기 때문에 생성자 block
    }

    public static <T> List<T> page(List<T> collection, Pageable pageable) {
        int pageNumber = pageable.getPageNumber();
        int pageSize = pageable.getPageSize();
        int fromIndex = pageNumber * pageSize;

        if (fromIndex >= collection.size()) {
            return Collections.emptyList();
        }
        return collection.subList(fromIndex, Math.min(fromIndex + pageSize, collection.size()));
    }
}
