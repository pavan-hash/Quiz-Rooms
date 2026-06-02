package com.pk.quizrooms.backend.enitity;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class QuizPlayedBy {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id ;
    @ManyToOne
    private User playedBy;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "quiz_id")
    private quiz quiz;
    private int score;
}

