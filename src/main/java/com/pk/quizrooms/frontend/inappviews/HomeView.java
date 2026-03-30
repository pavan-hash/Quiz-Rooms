package com.pk.quizrooms.frontend.inappviews;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.security.core.context.SecurityContextHolder;

@Route(value = "", layout = HomeLayout.class)
@PageTitle("Quiz-Rooms | Home")
@CssImport(value="./styles/home-view.css")
public class HomeView extends HorizontalLayout {

    public HomeView() {
        addClassName("home-view");
        // Title / greeting
        H2 title = new H2("Hey 👋! Let's start creating / playing a quiz 🎮");
        title.addClassName("home-title");

// Wrapper for the buttons
        HorizontalLayout buttonsRow = new HorizontalLayout();
        buttonsRow.addClassName("home-buttons-row");

// Buttons — each needs its specific class
        Button manualBtn = new Button("📝 Create Quiz Manually");
        manualBtn.addClassNames("home-btn", "home-btn-manual");

        Button aiBtn = new Button("🤖 Create Using AI");
        aiBtn.addClassNames("home-btn", "home-btn-ai");

        Button playBtn = new Button("🎮 Play Quiz");
//        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
//                .anyMatch(a -> a.getAuthority().equals("ADMIN"));
//        aiBtn.setVisible(isAdmin);
        playBtn.addClassNames("home-btn", "home-btn-play");
        manualBtn.addClickListener(e -> UI.getCurrent().navigate(CreateQuizManually.class));
        playBtn.addClickListener(e -> UI.getCurrent().navigate(QuizList.class));
        aiBtn.addClickListener(e->UI.getCurrent().navigate(AiQuizGeneratorView.class));
        buttonsRow.add(manualBtn, aiBtn, playBtn);
        add(title, buttonsRow);

    }
}