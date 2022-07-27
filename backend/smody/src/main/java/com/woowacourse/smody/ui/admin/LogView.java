package com.woowacourse.smody.ui.admin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.annotation.security.PermitAll;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import lombok.extern.slf4j.Slf4j;

@PageTitle("log")
@Route(value = "/admin/log", layout = MenuLayout.class)
@PermitAll
@Slf4j
public class LogView extends VerticalLayout {

	private static final String LOG_FILE_PATH = "./backend/smody/log/";
	private static final String BACKUP_LOG_FILE_PATH = "./backend/smody/log/backup/";
	private static final String MESSAGE_LINE_PREFIX = ">>>";

	private final VerticalLayout logLayout = new VerticalLayout();

	public LogView() {
		printTodayLog();
		Select<String> historySelect = createHistorySelect();
		add(
			logLayout,
			new HorizontalLayout(historySelect, createHistoryButton(historySelect))
		);
		arrangeComponents();
		historySelect.scrollIntoView();
	}

	private void printTodayLog() {
		try {
			String todayLogName = new File(LOG_FILE_PATH).list()[0];
			File todayLog = new File(LOG_FILE_PATH + todayLogName);
			convertFileToComponent(todayLog);
		} catch (Exception exception) {
			log.info("[로그 파일 예외 발생] 아직 오늘의 로그가 생성되지 않았습니다.");
		}
	}

	private Select<String> createHistorySelect() {
		Select<String> historySelect = new Select<>();
		try {
			String[] histories = new File(BACKUP_LOG_FILE_PATH).list();
			historySelect.setItems(histories);
			historySelect.setPlaceholder("이전 로그 기록");
		} catch (Exception ignored) {
		}
		return historySelect;
	}

	private Button createHistoryButton(Select<String> historySelect) {
		Button historyButton = new Button("선택");
		historyButton.addClickListener(event -> {
			logLayout.removeAll();
			convertFileToComponent(new File(BACKUP_LOG_FILE_PATH + historySelect.getValue()));
			historySelect.scrollIntoView();
		});
		return historyButton;
	}

	private void convertFileToComponent(File todayLog) {
		try {
			BufferedReader bufferedReader = new BufferedReader(new FileReader(todayLog));
			bufferedReader.lines().forEach(this::makeSpan);
		} catch (FileNotFoundException e) {
			log.warn("[로그 파일 예외 발생] 읽을 로그 파일이 없습니다.");
		}
	}

	private void makeSpan(String line) {
		Span span = new Span();

		for (LogLevel level : LogLevel.values()) {
			if (line.contains(level.getText())) {
				level.setColor(line, span);
			}
		}
		if (line.contains(MESSAGE_LINE_PREFIX)) {
			span.add(line);
		}
		logLayout.add(span);
	}

	private void arrangeComponents() {
		setMargin(true);
		setPadding(true);
		setSpacing(true);
		setWidth(97.9f, Unit.PERCENTAGE);
	}
}
