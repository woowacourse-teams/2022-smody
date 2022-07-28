package com.woowacourse.smody.ui.admin.domain;

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
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.repository.MemberRepository;
import com.woowacourse.smody.ui.admin.MenuLayout;
import javax.annotation.security.PermitAll;

@PageTitle("member")
@Route(value = "/admin/member", layout = MenuLayout.class)
@PermitAll
public class MemberView extends DomainView {

    private final MemberRepository memberRepository;
    private final String resourceName = "멤버";

    public MemberView(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
        add(
                new H3("모든 " + resourceName),
                createMembersLayout(),
                new H3(resourceName + " 생성"),
                createSaveLayout(),
                new H3(resourceName + " 삭제"),
                createDeleteLayout(memberRepository),
                createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<Member> createMembersLayout() {
        Grid<Member> membersGrid = new Grid<>();
        membersGrid.setItems(memberRepository.findAll());
        membersGrid.addColumn(Member::getId).setHeader("member_id");
        membersGrid.addColumn(Member::getEmail).setHeader("email");
        membersGrid.addColumn(Member::getNickname).setHeader("nickname");
        membersGrid.addColumn(Member::getIntroduction).setHeader("introduction");
        membersGrid.addColumn(Member::getPicture).setHeader("picture");
        return membersGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField emailField = createTextField("email");
        TextField nicknameField = createTextField("nickname");
        TextField introductionField = createTextField("introduction");
        TextField pictureField = createTextField("picture");
        saveForm.add(emailField, nicknameField, introductionField, pictureField);
        saveLayout.add(saveForm, createSaveButton(emailField, nicknameField, introductionField, pictureField));
        return saveLayout;
    }

    private Button createSaveButton(TextField emailField, TextField nicknameField, TextField introductionField,
                                    TextField pictureField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
                saveMember(emailField, nicknameField, introductionField, pictureField)
        );
        return saveButton;
    }

    private void saveMember(TextField emailField,
                            TextField nicknameField,
                            TextField introductionField,
                            TextField pictureField) {
        try {
            memberRepository.save(
                    new Member(
                            emailField.getValue(),
                            nicknameField.getValue(),
                            introductionField.getValue(),
                            pictureField.getValue()
                    )
            );
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }
}
