package com.woowacourse.smody.util;

import java.util.Collections;
import java.util.List;
import org.springframework.data.domain.Pageable;

public class PagingUtil<T> {

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
