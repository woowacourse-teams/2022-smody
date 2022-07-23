package com.woowacourse.smody.ui.admin.domain;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.domain.Cycle;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.Progress;
import com.woowacourse.smody.repository.ChallengeRepository;
import com.woowacourse.smody.repository.CycleRepository;
import com.woowacourse.smody.repository.MemberRepository;
import com.woowacourse.smody.ui.admin.MenuLayout;
import java.time.LocalDateTime;

@PageTitle("cycle")
@Route(value = "/admin/cycle", layout = MenuLayout.class)
public class CycleView extends DomainView {

    private final CycleRepository cycleRepository;
    private final MemberRepository memberRepository;
    private final ChallengeRepository challengeRepository;
    private final String resourceName = "사이클";

    public CycleView(CycleRepository cycleRepository,
                     MemberRepository memberRepository,
                     ChallengeRepository challengeRepository) {
        this.cycleRepository = cycleRepository;
        this.memberRepository = memberRepository;
        this.challengeRepository = challengeRepository;
        add(
                new H3("모든 " + resourceName),
                createChallengesGrid(),
                new H3(resourceName + " 생성"),
                createSaveLayout(),
                new H3(resourceName + " 삭제"),
                createDeleteLayout(cycleRepository),
                createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<Cycle> createChallengesGrid() {
        Grid<Cycle> challengeGrid = new Grid<>();
        challengeGrid.setItems(cycleRepository.findAll());
        challengeGrid.addColumn(Cycle::getId).setHeader("cycle_id");
        challengeGrid.addColumn(cycle -> cycle.getMember().getId()).setHeader("member_id");
        challengeGrid.addColumn(cycle -> cycle.getChallenge().getId()).setHeader("challenge_id");
        challengeGrid.addColumn(cycle -> cycle.getProgress().name()).setHeader("progress");
        challengeGrid.addColumn(Cycle::getStartTime).setHeader("start_time");
        return challengeGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField memberIdField = createTextField("member_id");
        TextField challengeIdField = createTextField("challenge_id");
        TextField progressField = createTextField("progress");
        DateTimePicker startTime = createDateTimePicker();
        saveForm.add(memberIdField, challengeIdField, progressField, startTime);
        saveLayout.add(saveForm, createSaveButton(memberIdField, challengeIdField, progressField, startTime));
        return saveLayout;
    }

    private Button createSaveButton(TextField memberIdField,
                                    TextField challengeIdField,
                                    TextField progressField,
                                    DateTimePicker startTimeField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
                saveCycle(memberIdField, challengeIdField, progressField, startTimeField)
        );
        return saveButton;
    }

    private void saveCycle(final TextField memberIdField,
                           final TextField challengeIdField,
                           final TextField progressField,
                           final DateTimePicker startTimeField) {
        try {
            Member member = memberRepository.findById(Long.parseLong(memberIdField.getValue())).get();
            Challenge challenge = challengeRepository.findById(Long.parseLong(challengeIdField.getValue())).get();
            Progress progress = Progress.valueOf(progressField.getValue().toUpperCase());
            LocalDateTime startTime = startTimeField.getValue();
            cycleRepository.save(
                    new Cycle(member, challenge, progress, startTime)
            );
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }

    private DateTimePicker createDateTimePicker() {
        DateTimePicker startTime = new DateTimePicker();
        startTime.setDatePlaceholder("사이클 시작 날짜");
        startTime.setTimePlaceholder("사이클 시작 시간");
        return startTime;
    }
}
