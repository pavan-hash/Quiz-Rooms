package com.pk.quizrooms.backend.service;


import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.question;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.repository.UserRepo;
import com.pk.quizrooms.backend.repository.questionRepo;
import com.pk.quizrooms.backend.repository.quizRepo;
import com.pk.quizrooms.backend.security.SecurityConfiguration;
import com.pk.quizrooms.frontend.inappviews.AiQuizGeneratorView;
import com.vaadin.frontendtools.internal.commons.compress.utils.ExactMath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Service
public class questionService  {

    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    private questionRepo qr;
    @Autowired
    private UserRepo userRepo;
    @Autowired
    private quizRepo quizRepo;
    public List<question> getQuestionsByUsername()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepo.findByUserName(auth.getName());
        List<question> questions = qr.findAllBycreatedBy(user);
        return questions;

    }
    public  List<question> getQuestions(Set<Integer> ids)
    {
        return  qr.findAllById(ids);
    }

    public void saveAll(List<question> questions)
    {
        qr.saveAll(questions);
    }

}
