package com.pk.quizrooms.backend.repository;


import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.question;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface questionRepo extends JpaRepository<question,Integer> {

    @Query(value = "SELECT * FROM question q Where q.category=:category ORDER BY RANDOM() LIMIT :numq", nativeQuery=true)
    List<question> findRandomQuestionsByCategory(String category,int numq);


    List<question> findAllBycreatedBy(User user);
}
