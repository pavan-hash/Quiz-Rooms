// ========================= AiQuizGeneratorService.java =========================
package com.pk.quizrooms.backend.service;

import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.question;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.core.type.TypeReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class AiQuizGeneratorService {

    private final ChatClient chatClient;
    private final ObjectMapper mapper;

    @Autowired
    public AiQuizGeneratorService(ChatClient.Builder chatClientBuilder, ObjectMapper mapper) {
        this.chatClient = chatClientBuilder.build();
        this.mapper = mapper;
    }

    public record GenerationParams(
            String topic,
            String category,
            question.difficulty difficulty,
            question.questionategory questionCategory,
            int numQuestions
    ) {}

    public List<question> generateQuestions(GenerationParams params, User createdBy) throws Exception {
        String prompt = buildPrompt(params);

        String rawJson = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return parseQuestions(rawJson, params, createdBy);
    }

    private String buildPrompt(GenerationParams p) {
        boolean isBoth = p.questionCategory() == question.questionategory.SingleAndMultipleAnswers;

        return """
            You are an expert quiz creator.
            Generate exactly %d quiz questions about the topic: "%s".

            Rules:
            - Category: %s
            - Difficulty: %s
            - Question type mode: %s

            Return ONLY a valid JSON array.

            Each element must have EXACTLY these fields:
            {
              "question_title": "The question text",
              "option1": "First option text",
              "option2": "Second option text",
              "option3": "Third option text",
              "option4": "Fourth option text",
              "question_type": "SingleAnswer or MultipleAnswers",
              "right_answer": "..."
            }

            %s

            Make questions clear and correct.
            """.formatted(
                p.numQuestions(),
                p.topic(),
                p.category(),
                p.difficulty().name(),
                p.questionCategory().name(),
                isBoth
                        ? """
                          For SingleAndMultipleAnswers:
                          - Each question MUST include question_type
                          - question_type = SingleAnswer OR MultipleAnswers
                          - SingleAnswer → one correct answer
                          - MultipleAnswers → comma-separated answers
                          """
                        : (p.questionCategory() == question.questionategory.SingleAnswer
                        ? "All questions must be SingleAnswer."
                        : "All questions must be MultipleAnswers.")
        );
    }

    private List<question> parseQuestions(String rawJson, GenerationParams params, User createdBy) throws Exception {
        String clean = rawJson.replaceAll("(?s)```json|```", "").trim();

        List<Map<String, Object>> rawList = mapper.readValue(clean, new TypeReference<>() {});

        List<question> result = new ArrayList<>();

        for (Map<String, Object> raw : rawList) {
            question q = new question();

            q.setQuestion_title((String) raw.get("question_title"));
            q.setOption1((String) raw.get("option1"));
            q.setOption2((String) raw.get("option2"));
            q.setOption3((String) raw.get("option3"));
            q.setOption4((String) raw.get("option4"));
            q.setRight_answer((String) raw.get("right_answer"));

            String typeStr = (String) raw.get("question_type");
            if (typeStr == null) typeStr = params.questionCategory().name();

            question.questionategory qType;

            if (params.questionCategory() == question.questionategory.SingleAndMultipleAnswers) {
                if ("MultipleAnswers".equalsIgnoreCase(typeStr)) {
                    qType = question.questionategory.MultipleAnswers;
                } else {
                    qType = question.questionategory.SingleAnswer;
                }
            } else {
                qType = params.questionCategory();
            }

            q.setQuestioncategory(qType);
            q.setDifficulty(params.difficulty());
            q.setCategory(params.category());
            q.setCreatedBy(createdBy);

            result.add(q);
        }

        return result;
    }
}


