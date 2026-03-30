package com.pk.quizrooms.frontend.entryviews;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.login.LoginForm;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.Collections;

@Route("login")
@PageTitle("Login | QUiz-Rooms")
@CssImport(value="./styles/login-view.css")
public class LoginView extends VerticalLayout implements BeforeEnterObserver {
    LoginForm loginForm = new LoginForm();

    public LoginView() {
        addClassName("login-view");
        loginForm.addClassName("loginform");
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        RouterLink register = new RouterLink("Not Signed up yet? Register Now", RegisterView.class);
        register.addClassName("register-link");
        loginForm.setAction("login");
        VerticalLayout card = new VerticalLayout(
                new H1("Welcome to Quiz Rooms"),
                new H1("Login to Enjoy the services :)"),
                loginForm,
                register
        );

        card.addClassName("login-card");
        card.setAlignItems(Alignment.CENTER);

        add(card);

    }



    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        if(!event.getLocation().getQueryParameters().getParameters().
                getOrDefault("error", Collections.emptyList()).isEmpty())
        {
            loginForm.setError(true);
        }

    }
}
