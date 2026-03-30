package com.pk.quizrooms.backend.repository;


import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface quizRepo extends JpaRepository<quiz,Integer> {


  

    Optional<quiz> findByquizhashid(String quizid);


    List<quiz> findAllByCreatedBy(User user);
}
