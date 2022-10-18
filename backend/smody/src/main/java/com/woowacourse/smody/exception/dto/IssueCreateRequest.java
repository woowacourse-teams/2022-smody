package com.woowacourse.smody.exception.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class IssueCreateRequest {

	private String title;
	private String body;
	private List<String> assignees;
	private List<String> labels;
}
