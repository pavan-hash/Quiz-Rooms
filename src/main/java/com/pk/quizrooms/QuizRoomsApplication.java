package com.pk.quizrooms;

import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dependency.StyleSheet;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.server.PWA;
import com.vaadin.flow.theme.lumo.Lumo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.webmvc.autoconfigure.error.ErrorMvcAutoConfiguration;


@Push
@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@StyleSheet(Lumo.STYLESHEET) // Use Aura.STYLESHEET to use Aura instead
@StyleSheet(Lumo.UTILITY_STYLESHEET)
@CssImport("./styles/home-view.css")
@PWA(name="Quiz Rooms",
        shortName ="QR",
offlinePath = "offline.html",
iconPath = "icons/quiz-rooms.png",
offlineResources = {"images/offline.jpg"})
//        offlinePath = "offline.html",
//        iconPath = "icons/CRM_icon.png",
//        offlineResources = {"images/offline.jpg"})
public class QuizRoomsApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(QuizRoomsApplication.class, args);
    }

}
