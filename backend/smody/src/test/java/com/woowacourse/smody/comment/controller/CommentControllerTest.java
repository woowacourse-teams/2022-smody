package com.woowacourse.smody.comment.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.comment.dto.CommentResponse;
import com.woowacourse.smody.comment.dto.CommentUpdateRequest;
import com.woowacourse.smody.support.ControllerTest;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

class CommentControllerTest extends ControllerTest {

    @DisplayName("댓글을 정상적으로 생성할 때 201을 응답한다.")
    @Test
    void create() throws Exception {
        // given
        Long commentId = 1L;
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        Map<String, String> param = Map.of("content", "화이팅!");

        given(commentService.create(any(TokenPayload.class), eq(1L), any(CommentRequest.class)))
                .willReturn(commentId);

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

    @DisplayName("비회원이 특정 피드의 모든 댓글을 조회할 때 200을 응답한다.")
    @Test
    void findAllNotLogin() throws Exception {
        // given
        List<CommentResponse> responses = List.of(
                new CommentResponse(1L, "토닉", "토닉.jpg", 1L, "화이팅1",
                        LocalDateTime.of(2022, 1, 1, 0, 0, 0), false),
                new CommentResponse(1L, "토닉", "토닉.jpg", 2L, "화이팅2",
                        LocalDateTime.of(2022, 1, 1, 1, 0, 0), false)
        );
        given(commentQueryService.findAllByCycleDetailId(any(TokenPayload.class), eq(1L)))
                .willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/feeds/1/comments"));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)))
                .andDo(document("get-all-comments", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("Member Id"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[].picture").type(JsonFieldType.STRING).description("사진"),
                                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("Comment Id"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성된 시간"),
                                fieldWithPath("[].isMyComment").type(JsonFieldType.BOOLEAN).description("내가 작성한 댓글인지")
                        ))
                );
    }

    @DisplayName("회원이 특정 피드의 모든 댓글을 조회할 때 200을 응답한다.")
    @Test
    void findAllLogin() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        List<CommentResponse> responses = List.of(
                new CommentResponse(1L, "토닉", "토닉.jpg", 1L, "화이팅1",
                        LocalDateTime.of(2022, 1, 1, 0, 0, 0), true),
                new CommentResponse(2L, "알파", "토닉.jpg", 2L, "화이팅2",
                        LocalDateTime.of(2022, 1, 1, 1, 0, 0), false)
        );

        given(commentQueryService.findAllByCycleDetailId(any(TokenPayload.class), eq(1L)))
                .willReturn(responses);

        // when
        ResultActions result = mockMvc.perform(get("/feeds/1/comments/auth")
                .header("Authorization", "Bearer " + token));
        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responses)))
                .andDo(document("get-all-comments-auth", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[].memberId").type(JsonFieldType.NUMBER).description("Member Id"),
                                fieldWithPath("[].nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[].picture").type(JsonFieldType.STRING).description("사진"),
                                fieldWithPath("[].commentId").type(JsonFieldType.NUMBER).description("Comment Id"),
                                fieldWithPath("[].content").type(JsonFieldType.STRING).description("댓글 내용"),
                                fieldWithPath("[].createdAt").type(JsonFieldType.STRING).description("생성된 시간"),
                                fieldWithPath("[].isMyComment").type(JsonFieldType.BOOLEAN).description("내가 작성한 댓글인지")
                        ))
                );
    }

    @DisplayName("댓글을 수정할 때 200을 응답한다.")
    @Test
    void updateComment() throws Exception {
        // given
        CommentUpdateRequest commentRequest = new CommentUpdateRequest("수정 내용");
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        // when
        ResultActions result = mockMvc.perform(patch("/comments/1")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + token)
                .content(objectMapper.writeValueAsString(commentRequest)));

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("update-comment", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("content").type(JsonFieldType.STRING).description("댓글 내용")
                        ))
                );
    }

    @DisplayName("댓글을 삭제할 때 204를 응답한다.")
    @Test
    void deleteComment() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        // when
        ResultActions result = mockMvc.perform(delete("/comments/1")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("delete-comment", HOST_INFO,
                        preprocessResponse(prettyPrint())
                ));
    }
}
