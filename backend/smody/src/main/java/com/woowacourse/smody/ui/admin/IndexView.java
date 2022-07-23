package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import com.woowacourse.smody.ui.admin.domain.MemberView;

@PageTitle("SMODY Admin")
@Route("")
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
public class IndexView extends VerticalLayout implements AppShellConfigurator {

    public static final String ADMIN_ENTRANCE_PASSWORD = "smody18!";

    public IndexView() {
        PasswordField passwordField = createPasswordField();
        Button adminLoginButton = createButton(passwordField);
        add(
                new H2("SMODY Admin"),
                passwordField,
                adminLoginButton
        );
        arrangeComponent();
    }

    private PasswordField createPasswordField() {
        PasswordField passwordField = new PasswordField("관리자 비밀번호");
        return passwordField;
    }

    private Button createButton(PasswordField passwordField) {
        Button adminLoginButton = new Button("로그인");
        adminLoginButton.addClickListener(event -> {
            if (passwordField.getValue().equals(ADMIN_ENTRANCE_PASSWORD)) {
                UI.getCurrent().navigate(MemberView.class);
            }
        });
        return adminLoginButton;
    }

    private void arrangeComponent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}
