package com.woowacourse.smody.acceptance;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.MediaType;

import com.woowacourse.smody.challenge.dto.ChallengeRequest;
import com.woowacourse.smody.comment.dto.CommentRequest;
import com.woowacourse.smody.cycle.dto.CycleRequest;
import com.woowacourse.smody.member.dto.MemberUpdateRequest;
import com.woowacourse.smody.push.dto.MentionNotificationRequest;
import com.woowacourse.smody.push.dto.SubscriptionRequest;
import com.woowacourse.smody.push.dto.UnSubscriptionRequest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

@SuppressWarnings("NonAsciiCharacters")
public class AcceptanceTestFixture {

    public static ExtractableResponse<Response> 나의_정보_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_정보_수정_요청(String token, String nickname, String introduction) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new MemberUpdateRequest(nickname, introduction))
            .when()
            .patch("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_이미지_수정_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .multiPart("profileImage", "image", "byte".getBytes())
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .when()
            .post("/members/me/profile-image")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_탈퇴_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .delete("/members/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 회원_조회_요청(String token, String filter) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .queryParams(buildDynamicParams(null, null, null, filter))
            .when()
            .get("/members/")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 사이클_생성_요청(String token, LocalDateTime startTime, Long challengeId) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token)
                .body(new CycleRequest(startTime, challengeId))
                .when()
                .post("/cycles")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사이클_진행_요청(String token, Long cycleId) {
        return RestAssured.given().log().all()
            .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
            .auth().oauth2(token)
            .multiPart("progressImage", "image", "byte".getBytes())
            .formParam("description", "인증")
            .when()
            .post("/cycles/" + cycleId + "/progress")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_사이클_조회_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/cycles/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 사이클_조회_요청(Long cycleId) {
        return RestAssured.given().log().all()
                .when()
                .get("/cycles/" + cycleId)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 사이클_통계_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/cycles/me/stat")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 챌린지별_사이클_조회_요청(String token, Long challengeId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/cycles/me/" + challengeId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 챌린지_조회_요청(Integer size, String sort, Long cursorId, String filter) {
        return RestAssured.given().log().all()
            .queryParams(buildDynamicParams(size, sort, cursorId, filter))
            .when()
            .get("/challenges")
            .then().log().all()
            .extract();
    }

    private static Map<String, Object> buildDynamicParams(Integer size, String sort, Long cursorId, String filter) {
        Map<String, Object> params = new HashMap<>();
        if (size != null) {
            params.put("size", size);
        }
        if (sort != null) {
            params.put("sort", sort);
        }
        if (cursorId != null) {
            params.put("cursorId", cursorId);
        }
        if (filter != null) {
            params.put("filter", filter);
        }
        return params;
    }

    public static ExtractableResponse<Response> 챌린지_조회_요청(Long challengeId) {
        return RestAssured.given().log().all()
            .when()
            .get("/challenges/" + challengeId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_챌린지_조회_요청(String token, int size, String sort, Long cursorId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .queryParam("size", size)
            .queryParam("sort", sort)
            .queryParam("cursorId", cursorId)
            .when()
            .get("/challenges/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_챌린지_조회_요청(String token, Long challengeId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/challenges/me/" + challengeId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 챌린저_조회_요청(Long challengeId) {
        return RestAssured.given().log().all()
            .when()
            .get("/challenges/" + challengeId + "/challengers")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 챌린지_생성_요청(String token, String name, String description) {
        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .auth().oauth2(token)
            .body(new ChallengeRequest(name, description, 1, 1))
            .when()
            .post("/challenges")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 피드_전체_조회_요청(Integer size, Long cursorId) {
        return RestAssured.given().log().all()
            .queryParams(buildDynamicParams(size, "latest", cursorId, null))
            .when()
            .get("/feeds")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 피드_조회_요청(Long feedId) {
        return RestAssured.given().log().all()
            .when()
            .get("/feeds/" + feedId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 댓글_작성_요청(String token, Long feedId, String content) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new CommentRequest(content))
            .when()
            .post("/feeds/" + feedId + "/comments")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 댓글_수정_요청(String token, Long commentId, String content) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new CommentRequest(content))
            .when()
            .patch("/comments/" + commentId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 댓글_삭제_요청(String token, Long commentId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .delete("/comments/" + commentId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 로그인_댓글_조회_요청(String token, Long feedId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/feeds/" + feedId + "/comments/auth")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 댓글_조회_요청(Long feedId) {
        return RestAssured.given().log().all()
            .when()
            .get("/feeds/" + feedId + "/comments")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 구글_링크_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/oauth/link/google")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 토큰_검증_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/oauth/check")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 알림_구독_요청(String token, String endpoint) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new SubscriptionRequest(endpoint, "p256dh", "auth"))
            .when()
            .post("/web-push/subscribe")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 알림_구독_해제_요청(String token, String endpoint) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new UnSubscriptionRequest(endpoint))
            .when()
            .post("/web-push/unsubscribe")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 맨션_알림_요청(String token, List<Long> memberIds, Long pathId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(new MentionNotificationRequest(memberIds, pathId))
            .when()
            .post("/push-notifications")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_알림_조회_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/push-notifications")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 알림_삭제_요청(String token, Long notificationId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .delete("/push-notifications/" + notificationId)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_알림_삭제_요청(String token) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .delete("/push-notifications/me")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 랭킹_기간_조회_요청() {
        return RestAssured.given().log().all()
            .queryParams(buildDynamicParams(10, "startDate:desc", null, null))
            .when()
            .get("/ranking-periods")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 랭킹_활동_조회_요청(Long periodId) {
        return RestAssured.given().log().all()
            .when()
            .get("/ranking-periods/" + periodId + "/ranking-activities")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 나의_랭킹_활동_조회_요청(String token, Long periodId) {
        return RestAssured.given().log().all()
            .auth().oauth2(token)
            .when()
            .get("/ranking-periods/" + periodId + "/ranking-activities/me")
            .then().log().all()
            .extract();
    }
}
