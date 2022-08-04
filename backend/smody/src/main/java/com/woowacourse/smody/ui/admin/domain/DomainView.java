package com.woowacourse.smody.ui.admin.domain;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.Notification.Position;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import org.springframework.data.jpa.repository.JpaRepository;

public abstract class DomainView extends VerticalLayout {

    protected <T> HorizontalLayout createDeleteLayout(JpaRepository<T, Long> repository) {
        HorizontalLayout deleteLayout = new HorizontalLayout();
        TextField deleteTextField = createTextField("삭제할 id");
        Button deleteButton = new Button("삭제");
        deleteButton.addClickListener(event -> {
                    deleteResourceById(repository, deleteTextField);
                }
        );
        deleteLayout.add(deleteTextField, deleteButton);
        return deleteLayout;
    }

    protected <T> void deleteResourceById(JpaRepository<T, Long> repository, TextField deleteTextField) {
        try {
            repository.deleteById(Long.parseLong(deleteTextField.getValue()));
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Position.BOTTOM_END);
        }
    }

    protected TextField createTextField(String value) {
        TextField emailField = new TextField();
        emailField.setPlaceholder(value);
        return emailField;
    }

    protected HorizontalLayout createFooterLayout() {
        HorizontalLayout footerLayout = new HorizontalLayout();
        Footer footer = new Footer(new Span("Copyright ⓒ 2022 - 2022 Team Smody. All rights reserved."));
        footerLayout.add(footer);
        footerLayout.setWidthFull();
        footerLayout.setHeight(66, Unit.PIXELS);
        footerLayout.setAlignItems(Alignment.END);
        footerLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return footerLayout;
    }

    protected void arrangeComponents() {
        setMargin(true);
        setPadding(true);
        setSpacing(true);
        setWidth(97.9f, Unit.PERCENTAGE);
    }
}
