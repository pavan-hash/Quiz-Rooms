package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.QuizPlayedBy;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.service.QuizPlayedByService;
import com.pk.quizrooms.backend.service.UserService;
import com.pk.quizrooms.backend.service.quizService;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "Profile", layout = HomeLayout.class)
@CssImport("./styles/profile-view.css")
public class ProfileView extends VerticalLayout {

    private final UserService userservice;
    private final quizService quizservice;
    private final QuizPlayedByService quizPlayedByservice;

    public ProfileView(UserService userservice,
                       quizService quizservice,
                       QuizPlayedByService quizPlayedByservice) {

        this.userservice = userservice;
        this.quizservice = quizservice;
        this.quizPlayedByservice = quizPlayedByservice;

        addClassName("profile-view");
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        setJustifyContentMode(JustifyContentMode.CENTER);

        // 🔷 CARD
        VerticalLayout card = new VerticalLayout();
        card.addClassName("profile-card");
        card.setAlignItems(Alignment.CENTER);

        // 🔷 HEADER (Avatar + Info)
        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("profile-header");

        Avatar avatar = new Avatar(userservice.getFullName());
        avatar.addClassName("profile-avatar");

        VerticalLayout userInfo = new VerticalLayout();
        userInfo.setPadding(false);
        userInfo.setSpacing(false);

        Span name = new Span(userservice.getFullName());
        name.addClassName("profile-name");

        Span emailText = new Span(userservice.getUserDetails().getEmail());
        emailText.addClassName("profile-email");

        userInfo.add(name, emailText);
        header.add(avatar, userInfo);

        // 🔷 FORM
        FormLayout form = new FormLayout();
        form.setWidthFull();

        TextField fullName = new TextField("Full Name");
        fullName.setValue(userservice.getFullName());
        fullName.setReadOnly(true);

        EmailField email = new EmailField("Email");
        email.setValue(userservice.getUserDetails().getEmail());
        email.setReadOnly(true);

        TextField phone = new TextField("Phone");
        phone.setValue(userservice.getUserDetails().getPhoneNumber());
        phone.setReadOnly(true);

        TextField username = new TextField("Username");
        username.setValue(userservice.getUserDetails().getUserName());
        username.setReadOnly(true);

        form.add(fullName, email, phone, username);

        // 🔷 STATS
        HorizontalLayout stats = new HorizontalLayout();
        stats.addClassName("profile-stats");

        int created = quizservice.getallQuizesforcurrentuser().size();
        int played = quizPlayedByservice.getAllQuizsPlayedBycurrentuser().size();

        stats.add(
                new Span("Created: " + created),
                new Span("Played: " + played)
        );

        // 🔷 ACTIONS
        Button changePassword = new Button("Change Password");
        Button createdBy = new Button("My Quizzes");
        Button playedBy = new Button("Played Quizzes");

        changePassword.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createdBy.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        playedBy.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        HorizontalLayout actions = new HorizontalLayout(changePassword, createdBy, playedBy);
        actions.addClassName("profile-actions");

        // 🔷 EVENTS
        changePassword.addClickListener(e -> openPasswordDialog());
        createdBy.addClickListener(e -> showCreatedQuizzes());
        playedBy.addClickListener(e -> showPlayedQuizzes());

        // 🔷 ADD ALL
        card.add(header, form, stats, actions);
        add(card);
    }

    // -------------------- DIALOGS --------------------

    private void showCreatedQuizzes() {
        Dialog dialog = new Dialog();
        dialog.setSizeFull();

        Grid<quiz> grid = new Grid<>(quiz.class, false);
        grid.addColumn(quiz::getTitle).setHeader("Title");
        grid.addColumn(quiz::getQuizhashid).setHeader("ID");
        grid.addColumn(q -> q.getQuestions().size()).setHeader("Questions");

        List<quiz> list = quizservice.getallQuizesforcurrentuser();
        grid.setItems(list);

        Button close = new Button("Close", e -> dialog.close());
        dialog.add(grid, close);
        dialog.open();
    }

    private void showPlayedQuizzes() {
        Dialog dialog = new Dialog();
        dialog.setSizeFull();

        Grid<QuizPlayedBy> grid = new Grid<>(QuizPlayedBy.class, false);
        grid.addColumn(q -> q.getQuiz().getTitle()).setHeader("Quiz");
        grid.addColumn(QuizPlayedBy::getScore).setHeader("Score");

        grid.setItems(quizPlayedByservice.getAllQuizsPlayedBycurrentuser());

        Button close = new Button("Close", e -> dialog.close());
        dialog.add(grid, close);
        dialog.open();
    }

    private void openPasswordDialog() {
        Dialog dialog = new Dialog();
        dialog.addClassName("custom-dialog");

        VerticalLayout layout = new VerticalLayout();
        layout.addClassName("dialog-card");

        Span title = new Span("Change Password");
        title.addClassName("dialog-title");

        TextField current = new TextField("Current Password");
        TextField next = new TextField("New Password");

        Button cancel = new Button("Cancel", e -> dialog.close());
        Button save = new Button("Save");

        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        save.addClickListener(e -> {
            if (userservice.passwordValidator(current.getValue())) {
                userservice.savedetails(next.getValue());
                Notification.show("Password changed!");
                dialog.close();
            } else {
                Notification.show("Wrong password");
            }
        });

        HorizontalLayout buttons = new HorizontalLayout(save, cancel);
        buttons.addClassName("dialog-actions");

        layout.add(title, current, next, buttons);
        dialog.add(layout);
        dialog.open();
    }
}