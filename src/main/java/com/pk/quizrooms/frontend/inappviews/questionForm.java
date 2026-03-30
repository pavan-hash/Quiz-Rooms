package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.question;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PreserveOnRefresh;
import com.vaadin.flow.router.Route;

@PreserveOnRefresh
@Route(layout=HomeLayout.class)
@CssImport(value="./styles/question-form-view.css")
public class questionForm extends FormLayout {
    Binder<question> questionbinder = new BeanValidationBinder<>(question.class);
    TextArea question_title = new TextArea("Question");
    TextField option1 = new TextField("Option1");
    TextField option2 = new TextField("Option2");
    TextField option3 = new TextField("Option3");
    TextField option4 = new TextField("Option4");
    TextField right_answer = new TextField("RightAnswer(s)");
    ComboBox<question.difficulty> difficulty = new ComboBox<>("Difficulty");
    ComboBox<question.questionategory>  questioncategory = new ComboBox<>("QuestionCategory");
    TextField category = new TextField("Category");
    Button save  = new Button("Save");
    Button cancel = new Button("Cancel");
    Button delete = new Button("Delete");
    
    
    public questionForm() {
        addClassName("questionForm-view");
        setResponsiveSteps(
                new ResponsiveStep("0", 1),
                new ResponsiveStep("600px", 2)
        );
        question_title.setHeight("120px");
        question_title.setPlaceholder("Enter your question here...");
        option1.setPlaceholder("Option A");
        option2.setPlaceholder("Option B");
        option3.setPlaceholder("Option C");
        option4.setPlaceholder("Option D");
        right_answer.setPlaceholder("Correct answer(s)");
        category.setPlaceholder("e.g. Java, Math, Science");
       questionbinder.bindInstanceFields(this);
       difficulty.setItems(question.difficulty.values());
       questioncategory.setItems(question.questionategory.values());
       add(question_title,option1,option2,option3,option4,questioncategory,right_answer,difficulty,category,createButtonsLayout());

    }

    private Component createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        delete.addClickShortcut(Key.DELETE);
        cancel.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        save.addClickShortcut(Key.ENTER);
        cancel.addClickShortcut(Key.ESCAPE);
        questionbinder.addStatusChangeListener(event -> {save.setEnabled(questionbinder.isValid());});

        return new VerticalLayout(save,delete,cancel);
    }

    public void setQuestion(question question) {
        questionbinder.setBean(question);
    }

    public question getQuestion()
    {
        return questionbinder.getBean();
    }
}
