package com.woowacourse.smody.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;

import com.woowacourse.smody.support.ControllerTest;

class BindingExceptionTest extends ControllerTest {

    @DisplayName("path variable 타입이 올바르지 않으면 400 예외가 발생한다.")
    @Test
    void invalidBinding_path() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/challenges/Nan"));

        // then
        result.andExpect(status().isBadRequest());
    }

    @DisplayName("param 타입이 올바르지 않으면 400 예외가 발생한다.")
    @Test
    void invalidBinding_param() throws Exception {
        // when
        ResultActions result = mockMvc.perform(get("/challenges?size=a"));

        // then
        result.andExpect(status().isBadRequest());
    }
}
