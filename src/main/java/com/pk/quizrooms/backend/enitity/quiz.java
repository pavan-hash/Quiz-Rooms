package com.pk.quizrooms.backend.enitity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class quiz {
    public enum requirepass{
        YES,NO;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int qid;
    private String title;
    @OneToMany(fetch = FetchType.EAGER)
    private List<question> questions;
    @ManyToOne
    @JoinColumn(name="username",referencedColumnName = "username")
    private User createdBy;
    private String quizhashid;
    private int duration;
    private double totalmarks;
    private double negativemarks;
    @Enumerated(EnumType.STRING)
    private quiz.requirepass requirepassword;
    private String quizpass;
//    @OneToMany
//    @JoinColumns({
//            @JoinColumn(name = "firstname", referencedColumnName = "firstname"),
//            @JoinColumn(name = "lastname", referencedColumnName = "lastname")
//    })
//    private List<User> playedBy;
//
//    private int score;
}
