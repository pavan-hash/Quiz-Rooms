package com.pk.quizrooms.backend.service;

import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepo userRepo;


    public void sendEmail(String body, String to, String subject)
    {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(to);
            mailMessage.setSubject("\uD83C\uDFAF Ready for Another Quiz Challenge?");
            mailMessage.setText(body);
            mailMessage.setFrom("quiz-rooms@gmail.com");
            mailSender.send(mailMessage);
            System.out.println("Mail sent successfully :)");
        }
        catch (Exception e) {
            System.out.println("Error sending email to "+to+" :(\n"+e.getMessage());
        }
    }

    public void SuccessfullRegistrationEmail(User user)
    {
                String body = "Hello " + user.getUserName() + ",\n" +
                        "\n" +
                        "Thank you for registering with Quiz Rooms!\n" +
                        "\n" +
                        "We're excited to have you join our community of quiz enthusiasts. You can now create quiz rooms, challenge your friends, and participate in engaging quizzes across a variety of topics.\n" +
                        "\n" +
                        "Here's what you can do next:\n" +
                        "\n" +
                        "✅ Create your own quiz room\n" +
                        "✅ Invite friends and compete together\n" +
                        "✅ Explore exciting quizzes\n" +
                        "✅ Track your progress and achievements\n" +
                        "\n" +
                        "Get started today and make learning fun through friendly competition.\n" +
                        "\n" +
                        "{{appLink}}\n" +
                        "\n" +
                        "If you have any questions or need assistance, feel free to reach out to us.\n" +
                        "\n" +
                        "Welcome aboard, and happy quizzing!\n" +
                        "\n" +
                        "Best Regards,\n" +
                        "Pavan Kumar Vadla \uD83D\uDE0E\n"+
                        "CEO of QUiz-Rooms \uD83E\uDDD1\u200D\uD83D\uDCBB";

            sendEmail(body, user.getEmail(), user.getPassword());



    }

    @Scheduled(cron = "0 0 9 * * FRI")
    public void sendRemainders() {
        List<User> users = new ArrayList<>();
        users = userRepo.findAll();
        String body = "Hello User,\n" +
                "\n" +
                "It's been a while since your last quiz session!\n" +
                "\n" +
                "Why not jump back into a quiz room and challenge your friends? Whether you're testing your knowledge, learning something new, or just having fun, quiz rooms are a great way to stay engaged and compete together.\n" +
                "\n" +
                "\uD83D\uDE80 Create or join a quiz room today.\n" +
                "\uD83C\uDFC6 Challenge your friends and climb the leaderboard.\n" +
                "\uD83E\uDDE0 Learn, compete, and have fun together.\n" +
                "\n" +
                "Start your next quiz adventure here:\n" +
                "\n" +
                "{{quizRoomLink}}\n" +
                "\n" +
                "See you in the quiz room!\n" +
                "\n" +
                "Best Regards,\n" +
                "Pavan Kumar Vadla \uD83D\uDE0E\n"+
                "CEO of QUiz-Rooms \uD83E\uDDD1\u200D\uD83D\uDCBB";
        for (User user : users) {
            body = body.replaceFirst("User",user.getUserName());
            sendEmail(body, user.getUserName(), user.getEmail());
        }

        }
    }

