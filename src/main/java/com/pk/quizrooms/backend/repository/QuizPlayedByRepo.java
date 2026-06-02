package com.pk.quizrooms.backend.repository;

import com.pk.quizrooms.backend.enitity.QuizPlayedBy;
import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizPlayedByRepo extends JpaRepository<QuizPlayedBy, Integer> {

    List<QuizPlayedBy> findAllByplayedBy(User user);


    List<QuizPlayedBy> findAllByquiz(quiz quiz);
}
