package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import javax.annotation.security.PermitAll;

@PageTitle("SMODY Admin")
@Route(value = "", layout = MenuLayout.class)
@Theme(themeClass = Lumo.class, variant = Lumo.DARK)
@PermitAll
public class IndexView extends VerticalLayout implements AppShellConfigurator {

    public IndexView() {
        add(
                new H1("This is SMODY Admin Page"),
                new H2("TEAM SMODY"),
                new H2("마르코, 빅터, 우연"),
                new H2("더즈, 토닉, 알파, 조조그린"),
                new H2("ENJOY!!!")
        );
        arrangeComponent();
    }

    private void arrangeComponent() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
    }
}
