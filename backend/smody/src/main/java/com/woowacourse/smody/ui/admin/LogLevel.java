package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.html.Span;

import lombok.Getter;

@Getter
public enum LogLevel {

	DEBUG("DEBUG", "aqua"),
	INFO("INFO", "lime"),
	WARN("WARN", "yellow"),
	ERROR("ERROR", "red"),
	EXCEPTION("Exception", "red")
	;

	private final String text;
	private final String color;

	LogLevel(String text, String color) {
		this.text = text;
		this.color = color;
	}

	public void setColor(String line, Span span) {
		String[] split = line.split(this.text);
		span.add(split[0]);

		Span info = new Span(this.text);
		info.getStyle().set("color", this.color);
		span.add(info);

		span.add(split[1]);

		span.getStyle().set("font-weight", "bold");
	}
}
