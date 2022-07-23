package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
import java.time.LocalDateTime;

@PageTitle("cycle")
@Route("/admin/cycle")
public class CycleView extends VerticalLayout {

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
                new MenuView(),
                new H3("모든 " + resourceName),
                createChallengesGrid(),
                new H3(resourceName + " 생성"),
                createSaveLayout(),
                new H3(resourceName + " 삭제"),
                createDeleteLayout()
        );
        arrangeComponent();
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
        DateTimePicker startTime = new DateTimePicker();
        startTime.setDatePlaceholder("사이클 시작 날짜");
        startTime.setTimePlaceholder("사이클 시작 시간");
        saveForm.add(memberIdField, challengeIdField, progressField, startTime);
        saveLayout.add(saveForm, createSaveButton(memberIdField, challengeIdField, progressField, startTime));
        return saveLayout;
    }

    private Button createSaveButton(TextField memberIdField,
                                    TextField challengeIdField,
                                    TextField progressField,
                                    DateTimePicker startTimeField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event -> {
                    Member member = memberRepository.findById(Long.parseLong(memberIdField.getValue())).get();
                    Challenge challenge = challengeRepository.findById(Long.parseLong(challengeIdField.getValue())).get();
                    Progress progress = Progress.valueOf(progressField.getValue().toUpperCase());
                    LocalDateTime startTime = startTimeField.getValue();
                    cycleRepository.save(
                            new Cycle(member, challenge, progress, startTime)
                    );
                    UI.getCurrent().getPage().reload();
                }
        );
        return saveButton;
    }

    private HorizontalLayout createDeleteLayout() {
        HorizontalLayout deleteLayout = new HorizontalLayout();
        TextField deleteTextField = createTextField("삭제할 id");
        Button deleteButton = new Button("삭제");
        deleteButton.addClickListener(event -> {
                    cycleRepository.deleteById(Long.parseLong(deleteTextField.getValue()));
                    UI.getCurrent().getPage().reload();
                }
        );
        deleteLayout.add(deleteTextField, deleteButton);
        return deleteLayout;
    }

    private TextField createTextField(final String value) {
        TextField emailField = new TextField();
        emailField.setPlaceholder(value);
        return emailField;
    }

    private void arrangeComponent() {
        setMargin(true);
        setPadding(true);
        setSpacing(true);
    }
}
