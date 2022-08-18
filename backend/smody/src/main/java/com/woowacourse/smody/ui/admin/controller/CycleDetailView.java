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
import com.woowacourse.smody.cycle.domain.Cycle;
import com.woowacourse.smody.cycle.domain.CycleDetail;
import com.woowacourse.smody.ui.admin.MenuLayout;
import com.woowacourse.smody.ui.admin.service.SmodyVaddinService;
import java.time.LocalDateTime;
import javax.annotation.security.PermitAll;

@PageTitle("cycleDetail")
@Route(value = "/admin/cycleDetail", layout = MenuLayout.class)
@PermitAll
public class CycleDetailView extends DomainView {

    private final SmodyVaddinService<CycleDetail> cycleDetailVaadinService;
    private final SmodyVaddinService<Cycle> cycleVaadinService;
    private final static String RESOURCE_NAME = "사이클 상세 정보";

    public CycleDetailView(SmodyVaddinService<CycleDetail> cycleDetailVaadinService,
                           SmodyVaddinService<Cycle> cycleVaadinService) {
        this.cycleDetailVaadinService = cycleDetailVaadinService;
        this.cycleVaadinService = cycleVaadinService;
        add(
                new H3("모든 " + RESOURCE_NAME),
                createCycleDetailLayout(),
                new H3(RESOURCE_NAME + " 생성"),
                createSaveLayout(),
                new H3(RESOURCE_NAME + " 삭제"),
                createDeleteLayout(cycleDetailVaadinService),
                createFooterLayout()
        );
        arrangeComponents();
    }

    private Grid<CycleDetail> createCycleDetailLayout() {
        Grid<CycleDetail> cycleDetailGrid = new Grid<>();
        cycleDetailGrid.setItems(cycleDetailVaadinService.findAll());
        cycleDetailGrid.addColumn(CycleDetail::getId).setHeader("cycleDetail_id");
        cycleDetailGrid.addColumn(cycleDetail -> cycleDetail.getCycle().getId()).setHeader("cycle_id");
        cycleDetailGrid.addColumn(CycleDetail::getProgressTime).setHeader("progress_time");
        cycleDetailGrid.addColumn(CycleDetail::getDescription).setHeader("description");
        cycleDetailGrid.addColumn(CycleDetail::getProgressImage).setHeader("progress_image");
        return cycleDetailGrid;
    }

    private HorizontalLayout createSaveLayout() {
        HorizontalLayout saveLayout = new HorizontalLayout();
        HorizontalLayout saveForm = new HorizontalLayout();
        TextField cycleIdField = createTextField("cycle_id");
        TextField descriptionField = createTextField("description");
        TextField progressImageField = createTextField("progress_image");
        DateTimePicker startTime = createDateTimePicker();
        saveForm.add(cycleIdField, descriptionField, progressImageField, startTime);
        saveLayout.add(saveForm, createSaveButton(
                cycleIdField, descriptionField, progressImageField, startTime));
        return saveLayout;
    }

    private DateTimePicker createDateTimePicker() {
        DateTimePicker startTime = new DateTimePicker();
        startTime.setDatePlaceholder("사이클 인증 날짜");
        return startTime;
    }

    private Button createSaveButton(TextField cycleIdField,
                                    TextField descriptionField,
                                    TextField progressImageField,
                                    DateTimePicker startTimeField) {
        Button saveButton = new Button("생성");
        saveButton.addClickListener(event ->
                saveCycleDetail(
                        cycleIdField, descriptionField, progressImageField, startTimeField)
        );
        return saveButton;
    }

    private void saveCycleDetail(TextField cycleIdField,
                                 TextField descriptionField,
                                 TextField progressImageField,
                                 DateTimePicker startTimeField) {
        try {
            Cycle cycle = cycleVaadinService.findById(Long.parseLong(cycleIdField.getValue())).get();
            LocalDateTime startTime = startTimeField.getValue();
            cycleDetailVaadinService.save(
                    new CycleDetail(cycle, startTime, progressImageField.getValue(), descriptionField.getValue())
            );
            UI.getCurrent().getPage().reload();
        } catch (Exception exception) {
            Notification.show(exception.getMessage(), 3000, Notification.Position.BOTTOM_END);
        }
    }
}
