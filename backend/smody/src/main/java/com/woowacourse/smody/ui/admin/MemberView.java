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
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.repository.MemberRepository;

@PageTitle("member")
@Route("/admin/member")
public class MemberView extends VerticalLayout {

    private final MemberRepository repository;
    private final String resourceName = "멤버";

    public MemberView(MemberRepository repository) {
        this.repository = repository;
        add(
                new MenuView(),
                new H3("모든 " + resourceName),
                createMembersGrid(),
                new H3(resourceName + " 생성"),
                createSaveLayout(),
                new H3(resourceName + " 삭제"),
                createDeleteLayout()
        );
        arrangeComponent();
    }

    private Grid<Member> createMembersGrid() {
        Grid<Member> membersGrid = new Grid<>();
        membersGrid.setItems(repository.findAll());
        membersGrid.addColumn(Member::getId).setHeader("id");
        membersGrid.addColumn(Member::getEmail).setHeader("email");
        membersGrid.addColumn(Member::getNickname).setHeader("nickname");
        membersGrid.addColumn(Member::getPicture).setHeader("picture");
        return membersGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout save = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField emailField = createTextField("email");
        TextField nicknameField = createTextField("nickname");
        TextField pictureField = createTextField("picture");
        saveForm.add(emailField, nicknameField, pictureField);
        save.add(saveForm, createSaveButton(emailField, nicknameField, pictureField));
        return save;
    }

    private Button createSaveButton(TextField emailField, TextField nicknameField, TextField pictureField) {
        Button saveButton = new Button("생성");
        Member member = new Member(emailField.getValue(), nicknameField.getValue(), pictureField.getValue());
        saveButton.addClickListener(event -> {
                    repository.save(member);
                    UI.getCurrent().getPage().reload();
                }
        );
        return saveButton;
    }

    private HorizontalLayout createDeleteLayout() {
        HorizontalLayout delete = new HorizontalLayout();
        TextField deleteTextField = createTextField("삭제할 id");
        Button deleteButton = new Button("삭제");
        deleteButton.addClickListener(event -> {
                    repository.deleteById(Long.parseLong(deleteTextField.getValue()));
                    UI.getCurrent().getPage().reload();
                }
        );
        delete.add(deleteTextField, deleteButton);
        return delete;
    }

    private TextField createTextField(final String email) {
        TextField emailField = new TextField();
        emailField.setPlaceholder(email);
        return emailField;
    }

    private void arrangeComponent() {
        setMargin(true);
        setPadding(true);
        setSpacing(true);
    }
}
