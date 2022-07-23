package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@PageTitle("SMODY Admin")
@Route("")
public class IndexView extends VerticalLayout {

    public IndexView() {
        TextField passwordTextField = createPassowordTextField();
        Button adminLoginButton = createButton(passwordTextField);
        add(
                new H2("SMODY Admin"),
                passwordTextField,
                adminLoginButton
        );
        arrangeComponent();
    }

    private void arrangeComponent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }

    private TextField createPassowordTextField() {
        TextField passwordTextField = new TextField("관리자 비밀번호");
        passwordTextField.setPlaceholder("password");
        return passwordTextField;
    }

    private Button createButton(TextField passwordTextField) {
        Button adminLoginButton = new Button("로그인");
        adminLoginButton.addClickListener(event -> {
            if (passwordTextField.getValue().equals("smody18!")) {
                UI.getCurrent().navigate(MemberView.class);
            }
        });
        return adminLoginButton;
    }
}
