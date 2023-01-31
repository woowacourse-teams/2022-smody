package com.woowacourse.smody;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MapTest {

    @DisplayName("빈 문자열도 키로 가질 수 있나?")
    @Test
    void empty_string_key() {
        // given
        Map<String, Boolean> map = new HashMap<>();
        map.put("", true);

        // when
        Boolean actual = map.get("");

        // then
        assertThat(actual).isTrue();
    }
}
