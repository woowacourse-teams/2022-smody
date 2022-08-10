package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.io.*;
import javax.annotation.security.PermitAll;
import lombok.extern.slf4j.Slf4j;

@PageTitle("log")
@Route(value = "/admin/log", layout = MenuLayout.class)
@PermitAll
@Slf4j
public class LogView extends VerticalLayout {

    private static final String LOG_FILE_PATH = "./log/";

    private final VerticalLayout logLayout = new VerticalLayout();

    public LogView() {
        Select<String> historySelect = createHistorySelect();
        add(
                logLayout,
                new HorizontalLayout(historySelect, createHistoryButton(historySelect))
        );
        arrangeComponents();
        historySelect.scrollIntoView();
    }

    private Select<String> createHistorySelect() {
        Select<String> historySelect = new Select<>();
        try {
            String[] histories = new File(LOG_FILE_PATH).list();
            historySelect.setItems(histories);
            historySelect.setPlaceholder("이전 로그 기록");
        } finally {

        }
        return historySelect;
    }

    private Button createHistoryButton(Select<String> historySelect) {
        Button historyButton = new Button("선택");
        historyButton.addClickListener(event -> {
            logLayout.removeAll();
            convertFileToComponent(new File(LOG_FILE_PATH + historySelect.getValue()));
            historySelect.scrollIntoView();
        });
        return historyButton;
    }

    private void convertFileToComponent(File todayLog) {
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(todayLog))) {
            bufferedReader.lines().forEach(this::makeSpan);
        } catch (FileNotFoundException e) {
            log.warn("[로그 파일 예외 발생] 읽을 로그 파일이 없습니다.");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void makeSpan(String line) {
        Span span = new Span();
        span.add(line);
        for (LogLevel level : LogLevel.values()) {
            if (line.contains(level.getText())) {
                span.removeAll();
                level.setColor(line, span);
            }
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
