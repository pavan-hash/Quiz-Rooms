package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.questionWrapper;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.service.quizService;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Route(value="playquiz")
@PageTitle("Quiz-rooms | play quiz")
@CssImport(value="./styles/play-quiz-view.css")
public class PlayQuiz extends VerticalLayout implements HasUrlParameter<String> {

    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> timerTask;
    private int remainingSeconds;
    int currentindex;
    Button next = new Button("Next");
    Button previous = new Button("Previous");
    Button submit = new Button("Submit");
    String quizid;
    HorizontalLayout buttons = new HorizontalLayout();
    Span timer = new Span("Time Remaining :");
    List<questionWrapper> questions;
    Map<Integer, String> answers = new HashMap<>();
    VerticalLayout mainLayout = new VerticalLayout();
    VerticalLayout questionContainer = new VerticalLayout();
     Span attemptedquestions = new Span("Attempted");
     Span notattemptedquestions = new Span("Not Attempted");
     Span totalquestions = new Span("Total Questions");
    int attempted= 0,notattempted=0;
    Button clearanswer = new Button("Clear Answer");
    quiz quiz;
    Span title = new Span();
    @Autowired
    private quizService quizService;

    public PlayQuiz(quizService quizService) throws InterruptedException {
        addClassName("playquiz-view");
        answers.clear();
        this.quizService = quizService;
        buttons.addClassName("quiz-controls");
        questionContainer.addClassName("question-container");
        attemptedquestions.addClassName("attempted");
        notattemptedquestions.addClassName("not-attempted");
        totalquestions.addClassName("total");
        timer.addClassName("timer");
        HorizontalLayout header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.add(title, timer);
        submit.setEnabled(false);
        buttons.setAlignItems(Alignment.CENTER);
        buttons.add(previous, next, submit,clearanswer);
        mainLayout.add(header, questionContainer, buttons,
                attemptedquestions, notattemptedquestions, totalquestions);
        setJustifyContentMode(JustifyContentMode.CENTER);
        clearanswer.addClickListener(event -> {clearcurrentanswer();});
        next.addClickListener(e -> {
            nextquestion();
        });
        previous.addClickListener(e -> {
            previousquestion();
        });
        submit.addClickListener(e -> {
            calculatescore(answers);
        });
    }

    private void clearcurrentanswer() {
        if(answers.containsKey(questions.get(currentindex).getId())) {
                answers.remove(questions.get(currentindex).getId());
                questionContainer.removeAll();
                questionContainer.add(displayQuestion(questions.get(currentindex)));

        }
    }


    private void previousquestion() {
        if (currentindex == 0)
            previous.setEnabled(false);
        else {
            currentindex--;
            submit.setEnabled(false);
            next.setEnabled(true);
            previous.setEnabled(true);
            questionContainer.removeAll();
            questionContainer.add(displayQuestion(questions.get(currentindex)));
        }
    }

    private void nextquestion() {
        if (currentindex == questions.size() - 1) {
            submit.setEnabled(true);
            next.setEnabled(false);
        } else {
            currentindex++;
            submit.setEnabled(false);
            next.setEnabled(true);
            previous.setEnabled(true);
            questionContainer.removeAll();
            questionContainer.add(displayQuestion(questions.get(currentindex)));
        }
    }


    private Component displayQuestion(questionWrapper question) {
        attempted = answers.size();
        notattempted = questions.size()-attempted;
        attemptedquestions.setText("Attempted Questions : "+String.valueOf(attempted));
        notattemptedquestions.setText("Not Attempted : "+String.valueOf(notattempted));
        if (questions.isEmpty()) {
            return new Span("No questions Available");
        }
        RadioButtonGroup<String> options = new RadioButtonGroup<>();
        CheckboxGroup<String> multipleoptions = new CheckboxGroup<>();
        next.setEnabled(true);
        VerticalLayout form = new VerticalLayout();
        form.setSizeFull();
        form.setWidthFull();
        form.setMaxWidth("800px");
        questionContainer.expand(form);
        form.setAlignItems(Alignment.CENTER);
        Span question_title = new Span(question.getQuestion_title());
        if (question.getQuestioncategory().equalsIgnoreCase("singleanswer")) {
            options.setItems(question.getOption1(), question.getOption2(),
                    question.getOption3(), question.getOption4());
            if (answers.containsKey(question.getId())) {
                options.setValue(answers.get(question.getId()));
            }


            options.addValueChangeListener(e -> {

                answers.put(question.getId(), e.getValue());
            });
            form.add(question_title, options);
            return form;
        } else {
            multipleoptions.setItems(question.getOption1(), question.getOption2(),
                    question.getOption3(), question.getOption4());
            if (answers.containsKey(question.getId())) {
                multipleoptions.setValue(Collections.singleton(answers.get(question.getId())));
            }


            multipleoptions.addValueChangeListener(e -> {
                answers.put(question.getId(), e.getValue().toString());
            });
            form.add(question_title, multipleoptions);
            return form;
        }
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, String s) {
        this.quizid = s;
        this.currentindex = 0;
        this.questions = quizService.playQuiz(quizid);
        this.quiz = quizService.getQuiz(quizid);
        System.out.println("Quiz ID: " + quizid);
        System.out.println("Questions: " + questions);
        if (questions == null) {
            questions = new ArrayList<>();
        }
        title.setText(quiz.getTitle());
        totalquestions.setText("Total Questions : "+String.valueOf(questions.size()));
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH);
        mainLayout.setAlignItems(Alignment.STRETCH);
        mainLayout.setWidthFull();
        mainLayout.setMaxWidth("1100px"); // optional center constraint
        mainLayout.getStyle().set("margin", "0 auto"); // center horizontally
        questionContainer.setSizeFull();
        mainLayout.expand(questionContainer);
        questionContainer.add(displayQuestion(questions.get(currentindex)));
        mainLayout.setSizeFull();
        expand(mainLayout);
        add(mainLayout);

    }


    public void calculatescore(Map<Integer, String> answers) {
        double score = quizService.calculatescore(answers, quizid);
        Dialog scoredialog = new Dialog("Your Score is :" + score);
        Button ok = new Button("Ok");
        scoredialog.add(ok);
        scoredialog.open();
        scoredialog.setCloseOnEsc(false);
        scoredialog.setCloseOnOutsideClick(false);
        scoredialog.setDraggable(false);
        ok.addClickListener(e -> {
            UI.getCurrent().navigate(HomeView.class);
            scoredialog.close();
        });

    }
    @Override
    protected void onAttach(AttachEvent attachEvent) {
        super.onAttach(attachEvent);

        if (quiz == null) return;

        remainingSeconds = quiz.getDuration() * 60;

        UI ui = attachEvent.getUI(); // ✅ ALWAYS use this

        scheduler = Executors.newSingleThreadScheduledExecutor();

        timerTask = scheduler.scheduleAtFixedRate(() -> {

            // ✅ STOP if UI is gone
            if (!ui.isAttached() || ui.isClosing()) {
                scheduler.shutdownNow();
                return;
            }

            if (remainingSeconds < 0) {

                ui.access(() -> {
                    if (!ui.isAttached()) return; // extra safety

                    timer.setText("Time's up!");
                    calculatescore(answers);
                });

                timerTask.cancel(true);
                scheduler.shutdown();
                return;
            }

            int minutes = remainingSeconds / 60;
            int seconds = remainingSeconds % 60;

            ui.access(() -> {
                if (!ui.isAttached()) return; // ✅ critical check

                timer.setText("Time Remaining: " + minutes + ":" + String.format("%02d", seconds));
            });

            remainingSeconds--;

        }, 0, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);

        if (timerTask != null && !timerTask.isCancelled()) {
            timerTask.cancel(true);
        }

        if (scheduler != null && !scheduler.isShutdown()) {
            scheduler.shutdownNow();
        }
    }

}