package com.pk.quizrooms.frontend.entryviews;

import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.service.UserService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.*;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import org.springframework.beans.factory.annotation.Autowired;

@PreserveOnRefresh
@Route("register")
@PageTitle("Register | Quiz-Rooms")
@CssImport(value="./styles/register-view.css")
public class RegisterView extends VerticalLayout {
    TextField firstName = new TextField("First Name");
    TextField middleName = new TextField("Middle Name");
    TextField lastName = new TextField("Last Name");
    DatePicker DOB = new DatePicker("DOB");
    TextField phoneNumber = new TextField("Phone Number");
    EmailField email = new EmailField("Email");
    TextField userName = new TextField("set UserName");
    PasswordField password = new PasswordField("set Password");
    Button submit = new Button("Submit");
    Binder<User> binder = new BeanValidationBinder<>(User.class);

    @Autowired
    UserService userService;
    public RegisterView(UserService userService) {
        addClassName("register-view");
        this.userService = userService;
        binder.addStatusChangeListener(event -> {submit.setEnabled(binder.isValid());});
        binder.bindInstanceFields(this);
        binder.setBean(new User());
        setSizeFull();
        submit.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        H1 title = new H1("Register Here :)");
        title.addClassName("title-register-view-title");
        submit.addClickListener(e -> {validateAndsaveUserDetails(binder.getBean());});
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        RouterLink login = new RouterLink("Already have an account? Login", LoginView.class);
        login.addClassName("back-login-link");
        VerticalLayout card = new VerticalLayout();
        card.addClassName("register-card");
        card.setAlignItems(Alignment.CENTER);

        FormLayout form = new FormLayout();
        form.addClassName("register-form");

        form.add(
                firstName, middleName, lastName,
                DOB, phoneNumber,
                email, userName, password
        );

// Make it responsive (2 columns desktop, 1 mobile)
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );


        card.add(title, form, submit, login);
        add(card);

    }

    private void validateAndsaveUserDetails(User user) {
        if(user!=null) {
            userService.savedetails(user);
            getUI().ifPresent(ui -> ui.navigate(LoginView.class));
        }
    }



    public void setUser(User user)
    {
        binder.setBean(user);
    }

    public User  getUser()
    {
        return binder.getBean();
    }
}
