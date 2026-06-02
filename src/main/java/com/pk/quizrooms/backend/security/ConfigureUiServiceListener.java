package com.pk.quizrooms.backend.security;



import com.pk.quizrooms.frontend.entryviews.LoginView;
import com.pk.quizrooms.frontend.entryviews.RegisterView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.server.ServiceInitEvent;
import com.vaadin.flow.server.VaadinServiceInitListener;
import org.springframework.stereotype.Component;


@Component
public class ConfigureUiServiceListener implements VaadinServiceInitListener {
    @Override
    public void serviceInit(ServiceInitEvent event) {
        event.getSource().addUIInitListener(UIEvent -> {
            final UI ui = UIEvent.getUI();
            ui.addBeforeEnterListener(this::beforeEnter);
        });

    }

    private void beforeEnter(BeforeEnterEvent event) {

        if(!LoginView.class.equals(event.getNavigationTarget())
                && !RegisterView.class.equals(event.getNavigationTarget())
                && !SecurityUtils.isUserLoggedIn()) {

            event.rerouteTo(LoginView.class);
        }
    }
    }

