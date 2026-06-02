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
    Button searchquiz = new Button("Search");

    quiz quiz;

    @Autowired
    quizService quizService;

    public QuizList(quizService quizService) {
        HorizontalLayout searchBar = new HorizontalLayout(search, searchquiz);
        searchBar.addClassName("search-bar");
        addClassName("quizlist-view");
        this.quizService = quizService;
        setSizeFull();
        quizgrid.removeAllColumns();
        search.setPlaceholder("Search Quiz by id");
        search.setClearButtonVisible(true);
        search.addKeyPressListener(Key.ENTER, e -> {
            findquiz(quizService.getQuiz(search.getValue()));
        });
        quizgrid.addItemDoubleClickListener(event -> {
            quiz = event.getItem();
        });
        searchquiz.addClickListener(event -> {
            findquiz(quizService.getQuiz(search.getValue()));
        });
        add(searchBar, quizgrid);

    }

    private void findquiz(quiz quiz) {
        quizgrid.removeAllColumns();
        if (quiz==null) {
           Notification notify= Notification.show("No quiz found \uD83D\uDE15");
           notify.addClassName("notification");
           notify.setPosition(Notification.Position.MIDDLE);
            quizgrid.setItems(); // clear grid

        } else {

            System.out.println(quiz.getTitle());
            quizgrid.addColumn(quizy -> quizy.getQuizhashid()).setHeader("ID");
            quizgrid.addColumn(quizy->quizy.getTitle()).setHeader("Name");
            quizgrid.addColumn(quizy -> quizy.getQuestions().size()).setHeader("No.of Questions");
            quizgrid.addComponentColumn(quizy -> {

                        Button play = new Button("Play quiz");
                        play.addClickListener(event -> {
                            Dialog dialog = new Dialog("Enter password to play");
                            if (quiz.getRequirepassword().toString().equalsIgnoreCase("yes")) {
                                dialog.open();
                                TextField pass = new TextField("Password");
                                Button ok = new Button("OK");
                                dialog.add(pass, ok);
                                ok.addClickListener(e -> {

                                    if (pass.getValue().equals(quiz.getQuizpass())) {
                                        dialog.close();
                                        UI.getCurrent().navigate(PlayQuiz.class, quizy.getQuizhashid());
                                    } else
                                        new Dialog("Enter correct password to play").open();
                                });
                            } else {
                                UI.getCurrent().navigate(PlayQuiz.class, quizy.getQuizhashid());
                            }

                        });
                return play;}).setHeader("");
            quizgrid.setItems(quiz);
        }

    }

}