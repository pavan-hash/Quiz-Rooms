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

    /*
     * Spring AI M3: ChatClient.Builder is the recommended injection point.
     * It is auto-configured by Spring Boot and wired to AnthropicChatModel
     * via your application.properties (spring.ai.anthropic.*).
     * No need to inject AnthropicChatModel directly.
     */
    @Autowired
    public AiQuizGeneratorService(ChatClient.Builder chatClientBuilder, ObjectMapper mapper) {
        this.chatClient = chatClientBuilder.build();
        this.mapper = mapper;
    }

    // ── Params record ─────────────────────────────────────────────────────────
    public record GenerationParams(
            String topic,
            String category,
            question.difficulty difficulty,
            question.questionategory questionCategory,
            int numQuestions
    ) {}

    // ── Main method ───────────────────────────────────────────────────────────
    public List<question> generateQuestions(GenerationParams params, User createdBy) throws Exception {
        String prompt = buildPrompt(params);

        String rawJson = chatClient.prompt()
                .user(prompt)
                .call()
                .content();

        return parseQuestions(rawJson, params, createdBy);
    }

    // ── Private: prompt builder ───────────────────────────────────────────────
    private String buildPrompt(GenerationParams p) {
        boolean isMulti = p.questionCategory() == question.questionategory.MultipleAnswers;

        return """
            You are an expert quiz creator.
            Generate exactly %d quiz questions about the topic: "%s".

            Rules:
            - Category/subject area: %s
            - Difficulty: %s
            - Question type: %s

            

            Return ONLY a valid JSON array — no markdown fences, no explanation, nothing else.
            Each element must have EXACTLY these fields:
            {
              "question_title": "The question text",
              "option1": "First option text (no A./1. prefix)",
              "option2": "Second option text",
              "option3": "Third option text",
              "option4": "Fourth option text",
              "right_answer": %s
            }

            %s

            Make questions clear, unambiguous, and appropriate for %s difficulty.
            """.formatted(
                p.numQuestions(),
                p.topic(),
                p.category(),
                p.difficulty().name(),
                isMulti ? "MultipleAnswers" : "SingleAnswer",
                isMulti
                        ? "For MultipleAnswers: right_answer must be a comma-separated string of the correct option texts (e.g. \"Paris,Berlin\")."
                        : "For SingleAnswer: right_answer must be exactly the text of the single correct option.",
                isMulti
                        ? "\"option1text,option2text\" (comma-separated correct answers)"
                        : "\"exact text of the correct option\"",
                p.difficulty().name()
        );
    }

    // ── Private: JSON → question entities ────────────────────────────────────
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
            q.setDifficulty(params.difficulty());
            q.setQuestioncategory(params.questionCategory());
            q.setCategory(params.category());
            q.setCreatedBy(createdBy);
            result.add(q);
        }
        return result;
    }
}
