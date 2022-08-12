package com.woowacourse.smody.controller;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.dto.TokenPayload;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class CommentControllerTest extends ControllerTest {

    @DisplayName("댓글을 정상적으로 생성할 때 201을 응답한다.")
    @Test
    void create() throws Exception {
        // given
        Long commentId = 1L;
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        Map<String, String> param = Map.of("content", "화이팅!");

        // when
        ResultActions result = mockMvc.perform(post("/feeds/1/comments")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(param)));
        // then
        result.andExpect(status().isCreated())
                .andExpect(header().string("Location", "/comments/" + commentId))
                .andDo(document("create-comment", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글")
                        )
                ));
    }
}
