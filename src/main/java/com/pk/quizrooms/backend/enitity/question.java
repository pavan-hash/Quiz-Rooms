package com.pk.quizrooms.backend.enitity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class question {
 public enum difficulty
    {
        EASY,MEDIUM,HARD;
    }
   public enum questionategory
   {
      SingleAnswer,MultipleAnswers, SingleAndMultipleAnswers;
   }
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    private String question_title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String right_answer;
    @Enumerated(EnumType.STRING)
    private question.difficulty difficulty ;
    @Enumerated(EnumType.STRING)
    private question.questionategory questioncategory;
    private String category;
    @ManyToOne
    @JoinColumn(name="username",referencedColumnName = "username")
    private User createdBy;
}
