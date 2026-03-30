package com.pk.quizrooms.backend.service;

import com.pk.quizrooms.backend.enitity.QuizPlayedBy;
import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.repository.QuizPlayedByRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizPlayedByService {


    private  QuizPlayedByRepo quizPlayedByRepo;
    private UserService userService;
    @Autowired
    public QuizPlayedByService(QuizPlayedByRepo quizPlayedByRepo, UserService userService) {
        this.quizPlayedByRepo = quizPlayedByRepo;
        this.userService = userService;
    }



    public void saveQuizPlayedBy(int score, quiz quiz, User playedBy)
    {
        QuizPlayedBy quizPlayedBy = new QuizPlayedBy();
        quizPlayedBy.setQuiz(quiz);
        quizPlayedBy.setPlayedBy(playedBy);
        quizPlayedBy.setScore(score);
        quizPlayedByRepo.save(quizPlayedBy);
    }

    public List<QuizPlayedBy> getAllQuizsPlayedBycurrentuser()
    {
        User user = userService.getUserDetails();
        List<QuizPlayedBy> quizsplayedby = quizPlayedByRepo.findAllByplayedBy(user);
        return quizsplayedby;
    }

    public List<QuizPlayedBy> getAllPlayerswhoplayedccurrentquiz(quiz quiz)
    {
        return quizPlayedByRepo.findAllByquiz(quiz);
    }
}
