package com.pk.quizrooms.backend.service;



import com.pk.quizrooms.backend.enitity.PrincipleUser;
import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.repository.UserRepo;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {


    @Autowired
    private UserRepo repo;

    @Override
    public UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {

        User user = repo.findByUserName(username);
        if(user == null  || !user.getUserName().equals(username)) {
            System.out.println("User 404 :(");
            throw new UsernameNotFoundException("User 404 :(");
        }


            return new PrincipleUser(user);

    }
}
