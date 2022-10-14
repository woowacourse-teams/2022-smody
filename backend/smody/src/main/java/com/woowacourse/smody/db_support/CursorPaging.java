package com.woowacourse.smody.db_support;

import java.util.List;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CursorPaging {

    public static <T> List<T> apply(List<T> list, T cursor, int size) {
        int totalSize = list.size();
        int startIndex = getStartIndex(list, cursor);
        int lastIndex = getLastIndex(totalSize, startIndex + size);
        return list.subList(startIndex, lastIndex);
    }

    private static <T> int getStartIndex(List<T> list, T cursor) {
        if (cursor == null) {
            return 0;
        }
        return list.indexOf(cursor) + 1;
    }

    private static int getLastIndex(Integer totalSize, Integer lastIndex) {
        return Math.min(totalSize, lastIndex);
    }
}
