package com.woowacourse.smody.acceptance;

import static com.woowacourse.smody.acceptance.AcceptanceTestFixture.*;
import static com.woowacourse.smody.support.ResourceFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.woowacourse.smody.member.dto.MemberResponse;
import com.woowacourse.smody.member.dto.SearchedMemberResponse;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
class MemberAcceptanceTest extends AcceptanceTest {

    @DisplayName("회원이 나의 정보를 조회한다.")
    @Test
    void get_members_me() {
        // when
        ExtractableResponse<Response> response = 나의_정보_요청(조조그린_토큰);

        // then
        MemberResponse actual = toResponseDto(response, MemberResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual.getNickname()).isEqualTo("조조그린")
        );
    }

    @DisplayName("나의 정보를 수정한다.")
    @Test
    void patch_members_me() {
        // given
        String 수정_닉네임 = "쬬그린";
        String 수정_소개 = "수정된 소개";

        // when
        ExtractableResponse<Response> response = 나의_정보_수정_요청(
            조조그린_토큰, 수정_닉네임, 수정_소개
        );

        // then
        MemberResponse actual = toResponseDto(나의_정보_요청(조조그린_토큰), MemberResponse.class);
        assertAll(
            NO_CONTENT_응답(response),
            () -> assertThat(actual.getNickname()).isEqualTo(수정_닉네임),
            () -> assertThat(actual.getIntroduction()).isEqualTo(수정_소개)
        );
    }

    @DisplayName("나의 프로필 이미지를 수정한다.")
    @Test
    void post_members_me_profile_image() {
        // when
        ExtractableResponse<Response> response = 나의_이미지_수정_요청(조조그린_토큰);

        // then
        assertAll(
            NO_CONTENT_응답(response)
        );
    }

    @DisplayName("회원 탈퇴를 한다.")
    @Test
    void delete_members_me() {
        //given
        사이클_진행_요청(조조그린_토큰,
            ID_추출(
                사이클_생성_요청(조조그린_토큰, LocalDateTime.now(), 미라클_모닝_ID)
            )
        );
        Long 피드_ID = 1L;
        댓글_작성_요청(조조그린_토큰, 피드_ID, "댓글");

        // when
        ExtractableResponse<Response> response = 회원_탈퇴_요청(조조그린_토큰);

        // then
        assertAll(
            NO_CONTENT_응답(response)
        );
    }

    @DisplayName("검색으로 회원들을 조회한다.")
    @Test
    void get_members() {
        // when
        ExtractableResponse<Response> response = 회원_조회_요청(조조그린_토큰, "더");

        // then
        List<SearchedMemberResponse> actual = toResponseDtoList(response, SearchedMemberResponse.class);
        assertAll(
            OK_응답(response),
            () -> assertThat(actual)
                .map(SearchedMemberResponse::getMemberId)
                .containsExactly(더즈_ID)
        );
    }
}
