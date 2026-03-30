package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.service.quizService;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

@Route(value="list",layout=HomeLayout.class)
@PageTitle("Quiz-rooms | quiz list")
@CssImport(value="./styles/quiz-list.css")
public class QuizList extends VerticalLayout {
    TextField search = new TextField("Search Quiz");
    Grid<quiz> quizgrid = new Grid<>(quiz.class);
    Dialog playdialog = new Dialog();
    Button searchquiz = new Button("Search");

    quiz quiz;

    @Autowired
    quizService quizService;

    public QuizList(quizService quizService) {
        HorizontalLayout searchBar = new HorizontalLayout(search, searchquiz);
        searchBar.addClassName("search-bar");
        addClassName("quizlist-view");
        playdialog.addClassName("play-dialog");
        this.quizService = quizService;
        setSizeFull();
        quizgrid.removeAllColumns();
        search.setPlaceholder("Search Quiz by id");
        search.setClearButtonVisible(true);
        search.addKeyPressListener(Key.ENTER, e -> {
            findquiz(quizService.getQuiz(search.getValue()));
        });
        Button play = new Button("Play quiz");
        Button cancel = new Button("Cancel");
        play.addClickListener(event -> {
            Dialog dialog = new Dialog("Enter password to play");
            if(quiz.getRequirepassword().toString().equalsIgnoreCase("yes")) {
                dialog.open();
                TextField pass = new TextField("Password");
                Button ok = new Button("OK");
                dialog.add(pass, ok);
                ok.addClickListener(e -> {

                    if (pass.getValue().equals(quiz.getQuizpass())) {
                        dialog.close();
                        UI.getCurrent().navigate(PlayQuiz.class, search.getValue());
                    } else
                        new Dialog("Enter correct password to play").open();
                });
            }
            else
                {
                    dialog.close();
                    UI.getCurrent().navigate(PlayQuiz.class, search.getValue());
                }

        });
        cancel.addClickListener(event -> {
            playdialog.close();
        });
        quizgrid.addItemDoubleClickListener(event -> {
            quiz = event.getItem();
            playdialog.open();
            playdialog.add(play, cancel);
        });
        searchquiz.addClickListener(event -> {
            findquiz(quizService.getQuiz(search.getValue()));
        });
        add(searchBar, quizgrid, playdialog);

    }

    private void findquiz(quiz quiz) {
        quizgrid.removeAllColumns();
        if (search.isEmpty()) {
           Notification notify= Notification.show("No quiz found \uD83D\uDE15");
           notify.addClassName("notification");
           notify.setPosition(Notification.Position.MIDDLE);
            quizgrid.setItems(); // clear grid

        } else {
//            if(quiz==null) {
//                new Notification("No Quiz Found");
//            }
//            else {
            System.out.println(quiz.getTitle());
            quizgrid.addColumn(com.pk.quizrooms.backend.enitity.quiz::getQuizhashid).setHeader("ID");
            quizgrid.addColumn(com.pk.quizrooms.backend.enitity.quiz::getTitle).setHeader("Name");
//            quizgrid.addColumn(quizy -> quizy.getCreatedBy().getFirstName() + quizy.getCreatedBy().getLastName()).setHeader("Created By");
            quizgrid.addColumn(quizy -> quizy.getQuestions().size()).setHeader("No.of Questions");
//            }
            quizgrid.setItems(quiz);
        }

    }

}