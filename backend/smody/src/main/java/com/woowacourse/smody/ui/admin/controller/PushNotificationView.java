package com.woowacourse.smody.ui.admin.controller;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.woowacourse.smody.domain.Member;
import com.woowacourse.smody.domain.PushCase;
import com.woowacourse.smody.domain.PushNotification;
import com.woowacourse.smody.domain.PushStatus;
import com.woowacourse.smody.ui.admin.MenuLayout;
import com.woowacourse.smody.ui.admin.service.SmodyVaddinService;

import javax.annotation.security.PermitAll;

@PageTitle("pushNotification")
@Route(value = "/admin/pushNotification", layout = MenuLayout.class)
@PermitAll
public class PushNotificationView extends DomainView {

    private final static String RESOURCE_NAME = "푸시 알림";

    private final SmodyVaddinService<PushNotification> pushNotificationVaadinService;
    private final SmodyVaddinService<Member> memberVaadinService;

    public PushNotificationView(
            SmodyVaddinService<PushNotification> pushNotificationVaadinService,
            SmodyVaddinService<Member> memberVaadinService) {
        this.pushNotificationVaadinService = pushNotificationVaadinService;
        this.memberVaadinService = memberVaadinService;
        add(
                new H3("모든 " + RESOURCE_NAME),
                createPushNotificationLayout(),
                new H3(RESOURCE_NAME + " 생성"),
                createSaveLayout(),
                new H3(RESOURCE_NAME + " 삭제"),
                createDeleteLayout(pushNotificationVaadinService),
                createFooterLayout()
        );
        arrangeComponents();
    }

	private Grid<PushNotification> createPushNotificationLayout() {
		Grid<PushNotification> pushNotificationGrid = new Grid<>();
		pushNotificationGrid.setItems(pushNotificationVaadinService.findAll());
		pushNotificationGrid.addColumn(PushNotification::getId).setHeader("push_notification_id");
		pushNotificationGrid.addColumn(PushNotification::getMessage).setHeader("message");
		pushNotificationGrid.addColumn(notification -> notification.getMember().getId())
			.setHeader("member_id");
		pushNotificationGrid.addColumn(PushNotification::getPushTime).setHeader("push_time");
		pushNotificationGrid.addColumn(PushNotification::getPushStatus).setHeader("push_status");
		pushNotificationGrid.addColumn(PushNotification::getPushCase).setHeader("push_case");
		pushNotificationGrid.addColumn(PushNotification::getPathId).setHeader("path_id");
		return pushNotificationGrid;
	}

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();

		TextField messageField = createTextField("message");
		TextField memberIdField = createTextField("member_id");
		DateTimePicker pushTimeField = createPushTimePicker();
		TextField pushStatusField = createTextField("push_status");
		TextField pushCaseField = createTextField("push_case");
		TextField pathIdField = createTextField("path_id");

		saveForm.add(messageField, memberIdField, pushStatusField, pushTimeField, pushCaseField, pathIdField);
		saveLayout.add(saveForm, createSaveButton(
			messageField, memberIdField, pushTimeField, pushStatusField, pushCaseField, pathIdField
		));
		return saveLayout;
	}

    private DateTimePicker createPushTimePicker() {
        DateTimePicker pushTime = new DateTimePicker();
        pushTime.setDatePlaceholder("알림 보낼 시간");
        return pushTime;
    }

	private Button createSaveButton(TextField messageField,
		TextField memberIdField,
		DateTimePicker pushTimeField,
		TextField pushStatusField,
		TextField pushCaseField,
		TextField pathIdField) {
		Button saveButton = new Button("생성");
		saveButton.addClickListener(event ->
			savePushNotification(
				messageField, memberIdField, pushTimeField, pushStatusField, pushCaseField, pathIdField)
		);
		return saveButton;
	}

	private void savePushNotification(TextField messageField,
		TextField memberIdField,
		DateTimePicker pushTimeField,
		TextField pushStatusField,
		TextField pushCaseField,
		TextField pathIdField) {

		try {
			Member member = memberVaadinService.findById(Long.parseLong(memberIdField.getValue()))
				.orElseThrow();
			PushStatus pushStatus = PushStatus.valueOf(pushStatusField.getValue());
			PushCase pushCase = PushCase.valueOf(pushCaseField.getValue());
			long pathId = Long.parseLong(pathIdField.getValue());
			pushNotificationVaadinService.save(new PushNotification(
				messageField.getValue(), pushTimeField.getValue(), pushStatus, member, pushCase, pathId
			));
			UI.getCurrent().getPage().reload();
		} catch (Exception exception) {
			Notification.show(exception.getMessage(), 3000, Notification.Position.BOTTOM_END);
		}
	}
}
