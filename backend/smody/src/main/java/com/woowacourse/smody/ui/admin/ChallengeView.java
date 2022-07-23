package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.woowacourse.smody.domain.Challenge;
import com.woowacourse.smody.repository.ChallengeRepository;

@PageTitle("challenge")
@Route("/admin/challenge")
public class ChallengeView extends VerticalLayout {

    private final ChallengeRepository challengeRepository;
    private final String resourceName = "챌린지";

    public ChallengeView(ChallengeRepository challengeRepository) {
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

    private Grid<Challenge> createChallengesGrid() {
        Grid<Challenge> challengeGrid = new Grid<>();
        challengeGrid.setItems(challengeRepository.findAll());
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
        saveButton.addClickListener(event -> {
                    challengeRepository.save(
                            new Challenge(nameField.getValue())
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
                    challengeRepository.deleteById(Long.parseLong(deleteTextField.getValue()));
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
