package com.pk.quizrooms.backend.repository;

import com.pk.quizrooms.backend.enitity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User,String> {
    User findByUserName(String username);
    User save(User user);

}
