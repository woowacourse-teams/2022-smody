package com.woowacourse.smody.exception;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GithubIssueGenerator {

	private static final String REPO_URL = "https://api.github.com/repos/woowacourse-teams/2022-smody/issues";
	private static final List<String> ASSIGNEES = List.of("ldk980130", "bcc0830", "jojogreen91", "tonic523");
	private static final List<String> LABELS = List.of("장애", "에러 해결", "백엔드");
	private static final MediaType JSON_MEDIA_TYPE = new MediaType("application", "json", StandardCharsets.UTF_8);

	@Value("${github.access.token}")
	private String accessToken;

	private final ObjectMapper objectMapper;

	public void create(Exception exception) {
		HttpEntity<String> httpEntity = new HttpEntity<>(createRequestJson(exception), setAuthorization());

		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(REPO_URL, HttpMethod.POST, httpEntity, String.class);
	}

	private String createRequestJson(Exception exception) {
		return parseJsonString(new IssueCreateRequest(
			"[ERROR] 서버 장애 발생 " + exception.getMessage(),
			createIssueBody(exception),
			ASSIGNEES,
			LABELS
		));
	}

	private HttpHeaders setAuthorization() {
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set("Authorization", "token " + accessToken);
		httpHeaders.setContentType(JSON_MEDIA_TYPE);
		return httpHeaders;
	}

	private String parseJsonString(IssueCreateRequest request) {
		String jsonData;
		try {
			jsonData = objectMapper.writeValueAsString(request);
		} catch (JsonProcessingException e) {
			throw new BusinessException(ExceptionData.DATA_INTEGRITY_ERROR);
		}
		return jsonData;
	}

	private String createIssueBody(Exception exception) {
		StringWriter stringWriter = new StringWriter();
		exception.printStackTrace(new PrintWriter(stringWriter));
		return "```\n" + stringWriter + "\n```";
	}
}
