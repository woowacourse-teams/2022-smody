package com.woowacourse.smody.member.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.request.RequestDocumentation.partWithName;
import static org.springframework.restdocs.request.RequestDocumentation.requestParts;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.woowacourse.smody.auth.dto.TokenPayload;
import com.woowacourse.smody.db_support.PagingParams;
import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.member.dto.SearchedMemberResponse;
import com.woowacourse.smody.support.ControllerTest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

public class MemberControllerTest extends ControllerTest {

    @DisplayName("나의 정보를 조회한다.")
    @Test
    void searchMyInfo() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        MemberResponse memberResponse = new MemberResponse(
                "alpha@naver.com", "손수건", "사진", "안녕하세요"
        );
        given(memberApiService.searchMyInfo(any(TokenPayload.class)))
                .willReturn(memberResponse);

        // when
        ResultActions result = mockMvc.perform(get("/members/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(memberResponse)))
                .andDo(document("get-my-info", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("picture").type(JsonFieldType.STRING).description("사진"),
                                fieldWithPath("email").type(JsonFieldType.STRING).description("이메일"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개글")
                        )));
    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void updateMyInfo() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        MemberUpdateRequest request = new MemberUpdateRequest("alpha", "Hello");

        // when
        ResultActions result = mockMvc.perform(patch("/members/me")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("update-my-info", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        requestFields(
                                fieldWithPath("nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("introduction").type(JsonFieldType.STRING).description("소개글")
                        )));
    }

    @DisplayName("회원의 프로필 이미지를 수정한다.")
    @Test
    void updateMyProfileImage() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        MockMultipartFile file = new MockMultipartFile(
                "profileImage", "test-profile-image.jpg", "image/jpg", "image".getBytes());

        // when
        ResultActions result = mockMvc.perform(multipart("/members/me/profile-image")
                .file(file)
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("update-profile-image", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        requestParts(
                                partWithName("profileImage").description("프로필 이미지")
                        )));
    }

    @DisplayName("회원을 탈퇴한다.")
    @Test
    void withdraw() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));

        // when
        ResultActions result = mockMvc.perform(delete("/members/me")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isNoContent())
                .andDo(document("withdraw", HOST_INFO,
                        preprocessResponse(prettyPrint())
                ));
    }

    @DisplayName("회원을 조회한다.")
    @Test
    void findAll() throws Exception {
        // given
        String token = jwtTokenProvider.createToken(new TokenPayload(1L));
        List<SearchedMemberResponse> searchedMemberRespons = List.of(
                new SearchedMemberResponse(3L, "알파", "사진"),
                new SearchedMemberResponse(5L, "알파쿤", "사진")
        );
        given(memberApiService.findAll(any(PagingParams.class)))
                .willReturn(searchedMemberRespons);

        // when
        ResultActions result = mockMvc.perform(get("/members?filter=알파&cursorId=2")
                .header("Authorization", "Bearer " + token));

        // then
        result.andExpect(status().isOk())
                .andDo(document("get-all-members", HOST_INFO,
                        preprocessResponse(prettyPrint()),
                        responseFields(
                                fieldWithPath("[]memberId").type(JsonFieldType.NUMBER).description("사용자 ID"),
                                fieldWithPath("[]nickname").type(JsonFieldType.STRING).description("닉네임"),
                                fieldWithPath("[]picture").type(JsonFieldType.STRING).description("사진")
                        )
                ));
    }
}
