package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.frontend.inappviews.HomeView;
import com.pk.quizrooms.frontend.inappviews.ProfileView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.RouterLink;

@CssImport(value="./styles/home-layout.css")
public class HomeLayout extends AppLayout {

    public HomeLayout() {
        addClassName("home-layout");
        createHeader();
        createDrawer();
    }

    private void createHeader() {
        addClassName("header");
        H1 header = new H1("Welcome to Quiz Rooms");
        HorizontalLayout headerLayout = new HorizontalLayout(new DrawerToggle(), header);
        headerLayout.setWidth("100%");
        addToNavbar(headerLayout);
    }

    private void createDrawer() {
        addClassName("drawer");
        Anchor logout =  new Anchor("/logout", "Logout");
        logout.setRouterIgnore(true);
        addToDrawer(
                new RouterLink("Home", HomeView.class),
                new RouterLink("Profile", ProfileView.class),logout);

    }

}