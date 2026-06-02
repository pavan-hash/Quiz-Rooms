package com.pk.quizrooms.backend.service;



import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.repository.UserRepo;
import com.pk.quizrooms.backend.security.SecurityConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    @Autowired
    private UserRepo repo;
    @Autowired
    private SecurityConfiguration securityConfiguration;

    @Autowired
    BCryptPasswordEncoder encoder;


    public void savedetails(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setAuthority(User.Roles.USER);
        repo.save(user);
    }

    public void savedetails(String newpassword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = repo.findByUserName(authentication.getName());
        user.setPassword(encoder.encode(newpassword));
        repo.save(user);

    }

    public User getUserDetails() {
        User user = new User();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            user = repo.findByUserName(authentication.getName());
        }
        return user;
    }

    public String getFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = repo.findByUserName(username);
            return user.getFirstName() + user.getMiddleName() + user.getLastName();
        } else
            return null;
    }

    public boolean passwordValidator(String current) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = new User();
        if (authentication != null&& authentication.isAuthenticated()) {
            String username = authentication.getName();
            user = repo.findByUserName(username);
            return encoder.matches(current, user.getPassword());
        }
        return false;
    }
}
