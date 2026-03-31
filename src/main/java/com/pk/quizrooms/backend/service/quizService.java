package com.pk.quizrooms.backend.service;


import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.pk.quizrooms.backend.enitity.*;
import com.pk.quizrooms.backend.repository.UserRepo;
import com.pk.quizrooms.backend.repository.quizRepo;
import com.pk.quizrooms.backend.security.SecurityConfiguration;
import com.pk.quizrooms.frontend.inappviews.AiQuizGeneratorView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class quizService implements AiQuizGeneratorView.OnSaveListener {

    String random_quiz_id;
    @Autowired
    SecurityConfiguration securityConfiguration;
    @Autowired
    private quizRepo qr;
    @Autowired
    private UserRepo userrepo;
    @Autowired
    private questionService questionService;
    @Autowired
    private QuizPlayedByService quizPlayedByService;


    public String create(List<question> questions, String quizname, String time,String totalmarks,String negativemarks,quiz.requirepass requirepass,String pass)
    {
        random_quiz_id = NanoIdUtils.randomNanoId();
        questionService.saveAll(questions);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        quiz quiz = new quiz();
        quiz.setTitle(quizname);
        quiz.setQuizhashid(random_quiz_id+username);
        quiz.setQuestions(questions);
        quiz.setCreatedBy(userrepo.findByUserName(username));
        quiz.setDuration(Integer.parseInt(time));
        quiz.setTotalmarks(Double.parseDouble(totalmarks));
        quiz.setNegativemarks(Double.parseDouble(negativemarks));
        quiz.setRequirepassword(requirepass);
        qr.save(quiz);
        return quiz.getQuizhashid();
    }

    public List<questionWrapper> playQuiz(String quizid) {
        Optional<quiz> quiz = qr.findByquizhashid(quizid);
        List<question> questions = quiz.get().getQuestions();
        System.out.println(questions);
        List<questionWrapper> questionwrapper = new ArrayList<>();
        for(question question : questions)
        {
            questionWrapper qw = new questionWrapper(question.getId(),question.getQuestion_title(),question.getOption1(),question.getOption2(),question.getOption3(),question.getOption4(),question.getQuestioncategory().toString());
            questionwrapper.add(qw);
        }
        return questionwrapper;
    }


    public quiz getQuiz(String quid) {
        return qr.findByquizhashid(quid).orElse(null);
    }

    public double calculatescore(Map<Integer, String> answers,String quizid) {
        quiz quiz = qr.findByquizhashid(quizid).orElse(null);
        double negativemarks=quiz.getNegativemarks();
        double  marksforrigthanswer=quiz.getTotalmarks()/(double)quiz.getQuestions().size();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int score=0;
        List<question> questions = questionService.getQuestions(answers.keySet());
        System.out.println(answers);
        System.out.println(questions);
        for(question question : questions)
        {
            if(question.getQuestioncategory().toString().equalsIgnoreCase("singleandmultipleanswers")
            || question.getQuestioncategory().toString().equalsIgnoreCase("multipleanswers")
            ) {
                String rightansweres=String.join( question.getRight_answer());
                String useranswers=String.join(answers.get(question.getId()));
                if(rightansweres.equals(useranswers))
                    score += marksforrigthanswer;
                else
                    score -= negativemarks;

            }
            else {
                if (question.getRight_answer().equals(answers.get(question.getId())))
                    score += marksforrigthanswer;
                else
                    score -= negativemarks;

            }
            }
        User user = userrepo.findByUserName(auth.getName());
        quizPlayedByService.saveQuizPlayedBy(score,quiz,user);
        return score;

    }
    public List<quiz> getallQuizesforcurrentuser()
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userrepo.findByUserName(auth.getName());
        List<quiz> quizes = qr.findAllByCreatedBy(user);
        return quizes;
    }



    @Override
    public void save(List<question> lastGenerated, String value, String value1, String value2, String value3, quiz.requirepass value4, String value5) {
        create(lastGenerated,value,value1,value2,value3,value4,value5);
    }
}
