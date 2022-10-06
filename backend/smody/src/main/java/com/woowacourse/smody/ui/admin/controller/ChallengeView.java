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
import com.woowacourse.smody.challenge.domain.Challenge;
import com.woowacourse.smody.ui.admin.MenuLayout;
import com.woowacourse.smody.ui.admin.service.SmodyVaddinService;

import javax.annotation.security.PermitAll;

@PageTitle("challenge")
@Route(value = "/admin/challenge", layout = MenuLayout.class)
@PermitAll
public class ChallengeView extends DomainView {

    private final static String RESOURCE_NAME = "챌린지";

    private final SmodyVaddinService<Challenge> vaddinChallengeService;

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
        challengeGrid.addColumn(Challenge::getDescription).setHeader("description");
        challengeGrid.addColumn(Challenge::getEmojiIndex).setHeader("emoji_index");
        challengeGrid.addColumn(Challenge::getColorIndex).setHeader("color_index");
        return challengeGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField nameField = createTextField("name");
        TextField descriptionField = createTextField("description");
        TextField emojiIndexField = createTextField("emoji_index");
        TextField colorIndexField = createTextField("color_index");
        saveForm.add(nameField, descriptionField, emojiIndexField, colorIndexField);
        saveLayout.add(saveForm, createSaveButton(nameField, descriptionField, emojiIndexField, colorIndexField));
        return saveLayout;
    }

    private Button createSaveButton(TextField nameField,
                                    TextField descriptionField,
                                    TextField emojiIndexField,
                                    TextField colorIndexField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
                saveChallenge(nameField, descriptionField, emojiIndexField, colorIndexField)
        );
        return saveButton;
    }

    private void saveChallenge(TextField nameField,
                               TextField descriptionField,
                               TextField emojiIndexField,
                               TextField colorIndexField) {
        try {
            vaddinChallengeService.save(
                    new Challenge(nameField.getValue(), descriptionField.getValue(),
                            Integer.parseInt(emojiIndexField.getValue()),
                            Integer.parseInt(colorIndexField.getValue())
                    ));
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }
}
