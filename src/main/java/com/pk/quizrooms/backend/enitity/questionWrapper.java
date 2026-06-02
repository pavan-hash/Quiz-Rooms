package com.pk.quizrooms.backend.enitity;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class questionWrapper {
    private int id;
    private String question_title;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String questioncategory;
}
