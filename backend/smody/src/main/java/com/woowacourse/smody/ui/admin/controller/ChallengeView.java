package com.woowacourse.smody.ui.admin.controller;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.ui.admin.MenuLayout;
import com.woowacourse.smody.ui.admin.service.SmodyVaddinService;

import javax.annotation.security.PermitAll;

@PageTitle("challenge")
@Route(value = "/admin/challenge", layout = MenuLayout.class)
@PermitAll
public class ChallengeView extends DomainView {

    private final SmodyVaddinService<Challenge> vaddinChallengeService;
    private final static String RESOURCE_NAME = "챌린지";

    public ChallengeView(SmodyVaddinService<Challenge> vaddinChallengeService) {
        this.vaddinChallengeService = vaddinChallengeService;
        add(
                new H3("모든 " + RESOURCE_NAME),
                createChallengesLayout(),
                new H3(RESOURCE_NAME + " 생성"),
                createSaveLayout(),
                new H3(RESOURCE_NAME + " 삭제"),
                createDeleteLayout(vaddinChallengeService),
                createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<Challenge> createChallengesLayout() {
        Grid<Challenge> challengeGrid = new Grid<>();
        challengeGrid.setItems(this.vaddinChallengeService.findAll());
        challengeGrid.addColumn(Challenge::getId).setHeader("challenge_id");
        challengeGrid.addColumn(Challenge::getName).setHeader("name");
        return challengeGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField nameField = createTextField("name");
        saveForm.add(nameField);
        saveLayout.add(saveForm, createSaveButton(nameField));
        return saveLayout;
    }

    private Button createSaveButton(TextField nameField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
                saveChallenge(nameField)
        );
        return saveButton;
    }

    private void saveChallenge(TextField nameField) {
        try {
            vaddinChallengeService.save(
                    new Challenge(nameField.getValue())
            );
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }
}
