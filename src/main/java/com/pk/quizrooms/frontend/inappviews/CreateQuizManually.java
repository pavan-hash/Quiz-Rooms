package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.question;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.service.UserService;
import com.pk.quizrooms.backend.service.questionService;
import com.pk.quizrooms.backend.service.quizService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

@PreserveOnRefresh
@Route(value="manualcreation",layout = HomeLayout.class)
@CssImport(value="./styles/create-quiz-manually-view.css")
@PageTitle("Quiz Rooms | Manual Creation")
public class CreateQuizManually extends VerticalLayout {
    Button createquiz = new Button("Create Quiz");
    Grid<question> questiongrid = new Grid<>(question.class);
    questionForm form;
    ArrayList<question> questions = new  ArrayList<>();
    AtomicLong counter = new AtomicLong(1);
    Dialog quiztitle = new Dialog();
    Button getpreviousquestions = new  Button("Get questions from any Previous quiz created by me");
    Button getall = new  Button("Get all questions created by me");

    @Autowired
    private questionService questionservice;
    @Autowired
    private quizService quizservice;
    @Autowired
    private UserService userservice;

    public CreateQuizManually(questionService questionservice, quizService quizservice) {

        addClassName("createquizmanually-view");
        quiztitle.addClassName("quiz-creation-title-details");
        this.questionservice = questionservice;
        this.quizservice = quizservice;
        setSizeFull();
        setAlignItems(Alignment.CENTER);
        configuregrid();
        form = new questionForm();
        form.addClassName("question-form");
        form.save.addClickListener(e -> {saveQuestion(form.getQuestion());
        });
        form.delete.addClickListener(e -> {deleteQuestion(form.getQuestion());
        });
        form.cancel.addClickListener(e -> {
            closeEditor();
        });
        createquiz.addClickListener(e->{validateandcreatequiz();});
        Div content = new Div(questiongrid, form);
        content.addClassName("content-div");
        content.setSizeFull();
        add(toolbar(), content);
        updateList();
        closeEditor();

    }

    private void validateandcreatequiz() {
        quiztitle.removeAll();
        if(questions.isEmpty() || questions.size()<5)
            new Dialog("please create more than five questions before creating").open();
        else
        {
            quiztitle.open();
            quiztitle.setWidth("40%");
            quiztitle.setHeight("40%");
            quiztitle.setResizable(false);
            quiztitle.setHeaderTitle("Enter the name for the quiz you will create");
            TextField quizname = new TextField("Name of the quiz");
            TextField timer = new TextField("Timer");
            timer.setPlaceholder("Enter Duration in minutes for the quiz");
            TextField totalmarks = new TextField("Total marks");
            totalmarks.setPlaceholder("Enter Total marks for the quiz");
            TextField negativemarks = new TextField("Negative marks");
            negativemarks.setPlaceholder("Enter Negative marks for the quiz");
            ComboBox<quiz.requirepass> requirepass = new ComboBox<>("Require Password to play?");
            requirepass.setItems(quiz.requirepass.values());
            requirepass.setRequired(true);
            TextField pass = new TextField("Password");
            pass.setPlaceholder("set pass for quiz");
            pass.setVisible(false);
            requirepass.addValueChangeListener(e->{
                if(requirepass.getValue().toString().equalsIgnoreCase("yes"))
                    pass.setVisible(true);

                else {
                    pass.setVisible(false);
                    pass.setValue("");
                }
            });
            Button ok = new Button("Ok");
            Button clear = new Button("Clear");
            clear.setVisible(false);
            Button cancel = new Button("cancel");
            quizname.setRequiredIndicatorVisible(true);
            quizname.setRequired(true);
            quizname.setPlaceholder("Enter the name for the quiz you will create");
            ok.addClickListener(e -> {
                String qhasid = quizservice.create(questions,quizname.getValue(),timer.getValue(),totalmarks.getValue(),negativemarks.getValue(), requirepass.getValue(),pass.getValue());
                new Dialog("Quiz created successfully, Please note your Quiz ID: "+qhasid).setCloseOnEsc(true);
                clear.setVisible(true);
                clear.addClickListener(es->{questions.clear();
                updateList();
                quiztitle.close();});

            });
            cancel.addClickListener(e -> {quiztitle.close();});
            counter.set(1);
            quiztitle.add(quizname,totalmarks,negativemarks,timer,requirepass,pass,ok,cancel,clear);
        }
    }

    public void saveQuestion(question question) {
        if (question == null)
            closeEditor();
        else {

                question.setCreatedBy(userservice.getUserDetails());
                questions.add(question);
                updateList();
                closeEditor();
            }
        }
    private void deleteQuestion(question question) {

        questions.remove(question);
        updateList();
        closeEditor();
    }

    private void updateList() {
        questiongrid.setItems(questions);
    }

    private Component toolbar() {
        AtomicReference<quiz> quiz = new AtomicReference<>(new quiz());
        Dialog quizes = new Dialog();
        quizes.setSizeFull();
        Button cancel = new Button("Cancel");
        Grid<quiz> quizgrid = new  Grid<>(quiz.class);
        quizgrid.removeAllColumns();
        quizes.add(quizgrid,cancel);
        cancel.addClickListener(e -> {quizes.close();});
        Button addQuestion = new Button("Add Question", e -> addQuestion());
        Button clear = new  Button("Clear",e->{questiongrid.setItems();
        questions.clear();
        });
      getall.addClickListener(e->{
          questions.clear();
          questions.addAll(questionservice.getQuestionsByUsername());
          questiongrid.setItems(questions);
      updateList();

      });
      getpreviousquestions.addClickListener(e->{quizes.open();
          quizgrid.addColumn(quizy->quizy.getQid()).setHeader("qid");
          quizgrid.addColumn(quizy->quizy.getCreatedBy().getUserName()).setHeader("CreatedBy");
          quizgrid.addColumn(quizy->quizy.getTitle()).setHeader("title");
          quizgrid.addColumn(quizy->quizy.getQuestions().size()).setHeader("No.of Questions");
          quizgrid.addColumn(quizy->quizy.getQuizhashid()).setHeader("quizhashid");
          quizgrid.setItems(quizservice.getallQuizesforcurrentuser());
          quizgrid.addItemDoubleClickListener(
              event-> {
                  quiz.set(quizservice.getQuiz(event.getItem().getQuizhashid()));
                  questions.clear();
                  questions.addAll(quiz.get().getQuestions());
                  questiongrid.setItems(questions);
                  updateList();
                  quizes.close();
              });

      });
      HorizontalLayout layout = new HorizontalLayout(addQuestion,createquiz,getpreviousquestions,getall,clear);
      layout.addClassName("toolbar");
      return layout;
    }

    private void addQuestion() {
        questiongrid.asSingleSelect().clear();
        editQuestion(new question());
    }

    private void closeEditor() {
        form.setVisible(false);
        form.setQuestion(null);
    }

    private void configuregrid() {

        questiongrid.addClassName("question-grid");
        questiongrid.setSizeFull();
        questiongrid.removeColumnByKey("id");
        questiongrid.removeColumnByKey("createdBy");
        questiongrid.setColumns("question_title", "option1", "option2", "option3","option4","right_answer",
                "difficulty","category");
        questiongrid.addColumn(question -> counter.getAndIncrement())
                .setHeader("q.no");
                questiongrid.getColumns().forEach(col -> col.setAutoWidth(true));
        questiongrid.asSingleSelect().addValueChangeListener(e -> {editQuestion(e.getValue());});


    }

    private void editQuestion(question value) {
        if (value == null) {
            form.setVisible(false);
            form.removeClassName("editing");
        } else {
            form.addClassName("editing");
            form.setVisible(true);
            form.setQuestion(value);

        }
    }
}
