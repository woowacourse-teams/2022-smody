package com.woowacourse.smody.ui.admin;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.RouterLink;
import com.woowacourse.smody.ui.admin.controller.ChallengeView;
import com.woowacourse.smody.ui.admin.controller.CycleDetailView;
import com.woowacourse.smody.ui.admin.controller.CycleView;
import com.woowacourse.smody.ui.admin.controller.LogView;
import com.woowacourse.smody.ui.admin.controller.MemberView;
import com.woowacourse.smody.ui.admin.controller.PushNotificationView;

public class MenuLayout extends AppLayout {

    public MenuLayout() {
        addToNavbar(createMenuTitle(), createTabs());
    }

    private H1 createMenuTitle() {
        H1 title = new H1("SMODY Admin");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("left", "var(--lumo-space-l)")
                .set("margin", "0")
                .set("position", "absolute");
        return title;
    }

    private Tabs createTabs() {
        Tabs tabs = new Tabs();
        tabs.getStyle().set("margin", "auto");
        tabs.add(
                createTab(VaadinIcon.USER, "member", MemberView.class),
                createTab(VaadinIcon.SWORD, "challenge", ChallengeView.class),
                createTab(VaadinIcon.RECYCLE, "cycle", CycleView.class),
                createTab(VaadinIcon.AIRPLANE, "cycleDetail", CycleDetailView.class),
                createTab(VaadinIcon.ALARM, "pushNotification", PushNotificationView.class),
                createTab(VaadinIcon.BUG, "log", LogView.class)
        );
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName, Class<? extends Component> navigateTarget) {
        Icon icon = viewIcon.create();
        icon.getStyle()
                .set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        link.setRoute(navigateTarget);
        link.setTabIndex(-1);
        return new Tab(link);
    }
}
