package com.pk.quizrooms.frontend.inappviews;

import com.pk.quizrooms.backend.enitity.User;
import com.pk.quizrooms.backend.enitity.question;
import com.pk.quizrooms.backend.enitity.quiz;
import com.pk.quizrooms.backend.repository.UserRepo;
import com.pk.quizrooms.backend.service.AiQuizGeneratorService;
import com.pk.quizrooms.backend.service.AiQuizGeneratorService.GenerationParams;
import com.pk.quizrooms.backend.service.quizService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.dialog.DialogVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.progressbar.ProgressBar;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Vaadin Flow view — AI Quiz Generator.
 *
 * TODO: Replace the @Route layout with your actual main layout shell.
 * TODO: Inject the logged-in User via your SecurityService / session bean.
 */
@PageTitle("AI Quiz Generator")
@Route(value = "ai-quiz-generator",layout= HomeLayout.class)// add: layout = YourMainLayout.class
@CssImport(value="./styles/ai-quiz-generator.css")
public class AiQuizGeneratorView extends VerticalLayout {

    // ── Dependencies ───────────────────────────────────────────────────────────

    // ── Form fields ────────────────────────────────────────────────────────────
    private  TextField   topicField      = new TextField("Topic");
    private  TextField   categoryField   = new TextField("Category");
    private  Select<question.difficulty>         diffSelect  = new Select<>();
    private  Select<question.questionategory>    typeSelect  = new Select<>();
    private  IntegerField   countField      = new IntegerField("Number of Questions");

    // ── Buttons ────────────────────────────────────────────────────────────────
    private  Button generateBtn   = new Button("✦  Generate Questions");
    private  Button toggleAnswers = new Button("Show Answers");
    private  Button regenerateBtn = new Button("↻  Regenerate");
    private  Button saveAllBtn    = new Button("✓  Create Quiz");

    // ── State ──────────────────────────────────────────────────────────────────
    private  ProgressBar      progressBar    = new ProgressBar();
    private  VerticalLayout   resultsSection = new VerticalLayout();
    private List<question>         lastGenerated  = List.of();
    private final AtomicBoolean    showAnswers    = new AtomicBoolean(false);

    // ── Callback: wire this to your QuestionRepository / QuestionService ───────
    public interface OnSaveListener {
        void save(List<question> lastGenerated, String value, String value1, String value2, String value3, quiz.requirepass value4, String value5);


    }
    @Autowired
    private OnSaveListener onSaveListener;
    public void setOnSaveListener(OnSaveListener listener) { this.onSaveListener = listener; }

    // ── The logged-in user (inject via your SecurityService) ───────────────────
    private User currentUser;
    public void setCurrentUser(User user) { this.currentUser = user; }

    @Autowired
    private AiQuizGeneratorService aiService;
    @Autowired
    private UserRepo userrepo;

    @Autowired
    private quizService quizservice;

    // ── Constructor ────────────────────────────────────────────────────────────
    public AiQuizGeneratorView(AiQuizGeneratorService aiService,UserRepo userepo) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        this.aiService = aiService;
        User currentuser= userepo.findByUserName(username);
        setCurrentUser(currentuser);
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        getStyle()
                .set("background",   "#0a0a0f")
                .set("color",        "#e8e8f0")
                .set("font-family",  "'Inter', sans-serif")
                .set("padding",      "24px")
                .set("max-width",    "860px")
                .set("margin",       "0 auto");

        injectGoogleFont();
        add(buildHeader(), buildFormCard(), buildProgressRow(), resultsSection);
    }

    // ── Header ─────────────────────────────────────────────────────────────────

    private VerticalLayout buildHeader() {
        Span badge = new Span("⬡  AI-Powered");
        styleInline(badge,
                "background:rgba(124,106,255,.12);border:1px solid rgba(124,106,255,.3);" +
                        "color:#7c6aff;font-size:11px;letter-spacing:.1em;text-transform:uppercase;" +
                        "padding:5px 14px;border-radius:100px;display:inline-block;margin-bottom:12px");

        H2 title = new H2("AI Quiz Generator");
        title.addClassName("ai-title");

        Paragraph sub = new Paragraph("Pick your parameters — AI writes the questions.");
        styleInline(sub, "color:#7070a0;font-size:14px;margin:0");

        VerticalLayout header = new VerticalLayout(badge, title, sub);
        header.setAlignItems(Alignment.CENTER);
        header.setPadding(false);
        header.setSpacing(false);
        styleInline(header, "text-align:center;margin-bottom:28px;padding-top:8px");
        return header;
    }

    // ── Form card ──────────────────────────────────────────────────────────────

    private VerticalLayout buildFormCard() {
        VerticalLayout card = new VerticalLayout();
        styleInline(card,
                "background:#111118;border:1px solid #2a2a3a;border-radius:20px;" +
                        "padding:28px;margin-bottom:20px");
        card.setSpacing(true);
        card.setPadding(false);

        // ── Row 1: Topic + Category ────────────────────────────────────────────
        topicField.setPlaceholder("e.g. Java Collections Framework");
        topicField.setWidthFull();
        applyFieldTheme(topicField);

        categoryField.setPlaceholder("e.g. Programming");
        categoryField.setWidthFull();
        applyFieldTheme(categoryField);

        HorizontalLayout row1 = row(topicField, categoryField);

        // ── Row 2: Difficulty + Type + Count ──────────────────────────────────
        diffSelect.setLabel("Difficulty");
        diffSelect.setItems(question.difficulty.values());
        diffSelect.setValue(question.difficulty.MEDIUM);
        diffSelect.setItemLabelGenerator(d -> switch (d) {
            case EASY   -> "Easy";
            case MEDIUM -> "Medium";
            case HARD   -> "Hard";
        });
        diffSelect.setWidthFull();
        applyFieldTheme(diffSelect);

        typeSelect.setLabel("Question Type");
        typeSelect.setItems(question.questionategory.values());
        typeSelect.setValue(question.questionategory.SingleAnswer);
        typeSelect.setItemLabelGenerator(t -> switch (t) {
            case SingleAnswer   -> "Single Answer";
            case MultipleAnswers -> "Multiple Answers";
            case SingleAndMultipleAnswers -> "Single and Multiple Answers";
        });
        typeSelect.setWidthFull();
        applyFieldTheme(typeSelect);

        countField.setMin(3);
        countField.setMax(20);
        countField.setValue(5);
        countField.setStepButtonsVisible(true);
        countField.setWidthFull();
        applyFieldTheme(countField);

        HorizontalLayout row2 = row(diffSelect, typeSelect, countField);

        // ── Generate button ────────────────────────────────────────────────────
        generateBtn.setWidthFull();
        styleInline(generateBtn,
                "background:linear-gradient(135deg,#7c6aff,#9b6aff);color:#fff;" +
                        "font-weight:700;font-size:15px;border-radius:12px;padding:14px;" +
                        "border:none;cursor:pointer;box-shadow:0 8px 32px rgba(124,106,255,.35);margin-top:8px");
        generateBtn.addClickListener(e -> runGeneration());

        card.add(sectionLabel("Topic & Category"), row1,
                sectionLabel("Configuration"),     row2,
                generateBtn);
        return card;
    }

    // ── Progress bar ───────────────────────────────────────────────────────────

    private Div buildProgressRow() {
        progressBar.setIndeterminate(true);
        progressBar.setVisible(false);
        progressBar.setWidthFull();
        Div d = new Div(progressBar);
        d.setWidthFull();
        styleInline(d, "margin-bottom:8px");
        return d;
    }

    // ── Generation ─────────────────────────────────────────────────────────────

    private void runGeneration() {
        String topic    = topicField.getValue();
        String category = categoryField.getValue();

        if (topic == null || topic.isBlank()) {
            notify("Please enter a topic.", NotificationVariant.LUMO_ERROR);
            return;
        }
        if (category == null || category.isBlank()) {
            notify("Please enter a category.", NotificationVariant.LUMO_ERROR);
            return;
        }

        GenerationParams params = new GenerationParams(
                topic,
                category,
                diffSelect.getValue(),
                typeSelect.getValue(),
                countField.getValue() != null ? countField.getValue() : 5
        );

        generateBtn.setEnabled(false);
        progressBar.setVisible(true);
        resultsSection.removeAll();

        getUI().ifPresent(ui -> Thread.ofVirtual().start(() -> {
            try {
                List<question> generated = aiService.generateQuestions(params, currentUser);
                ui.access(() -> {
                    lastGenerated = generated;
                    showAnswers.set(false);
                    progressBar.setVisible(false);
                    generateBtn.setEnabled(true);
                    renderResults(generated, params);
                });
            } catch (Exception ex) {
                ui.access(() -> {
                    progressBar.setVisible(false);
                    generateBtn.setEnabled(true);
                    notify("Generation failed: " + ex.getMessage(), NotificationVariant.LUMO_ERROR);
                });
            }
        }));
    }

    // ── Results ────────────────────────────────────────────────────────────────

    private void renderResults(List<question> questions, GenerationParams params) {
        resultsSection.removeAll();
        resultsSection.setPadding(false);
        resultsSection.setSpacing(true);

        // Action bar
        Span countLabel = new Span(questions.size() + " Questions Generated");
        styleInline(countLabel, "font-weight:700;font-size:17px");

        toggleAnswers.setText(showAnswers.get() ? "Hide Answers" : "Show Answers");
        styleSecondaryBtn(toggleAnswers);
        toggleAnswers.addClickListener(e -> {
            showAnswers.set(!showAnswers.get());
            renderResults(lastGenerated, params);
        });

        styleSecondaryBtn(regenerateBtn);
        regenerateBtn.addClickListener(e -> runGeneration());

        styleInline(saveAllBtn,
                "background:rgba(106,255,218,.1);color:#6affda;" +
                        "border:1px solid rgba(106,255,218,.3);border-radius:10px;" +
                        "font-weight:600;cursor:pointer;padding:8px 18px");
        saveAllBtn.addClickListener(e -> {
            if (onSaveListener != null) {
                Dialog aiquizcreationparameters =  new Dialog("Enter parameters to create quiz with these questioons");
                VerticalLayout fieldslayout = new VerticalLayout();
                TextField quizname = new TextField("Name of the quiz");
                quizname.setRequired(true);
                TextField timer = new TextField("Timer");
                timer.setRequired(true);
                timer.setPlaceholder("Enter Duration in minutes for the quiz");
                TextField totalmarks = new TextField("Total marks");
                totalmarks.setRequired(true);
                totalmarks.setPlaceholder("Enter Total marks for the quiz");
                TextField negativemarks = new TextField("Negative marks");
                negativemarks.setRequired(true);
                negativemarks.setPlaceholder("Enter Negative marks for the quiz");
                ComboBox<quiz.requirepass> requirepass = new ComboBox<>("Require Password to play?");
                requirepass.setItems(quiz.requirepass.values());
                requirepass.setRequired(true);
                TextField pass = new TextField("Password");
                pass.setPlaceholder("set pass for quiz");
                pass.setRequired(true);
                Button ok = new Button("Ok");
                Button clear = new Button("Clear");
                clear.setVisible(false);
                Button cancel = new Button("cancel");
                pass.setVisible(false);
                fieldslayout.add(quizname,totalmarks,negativemarks,timer,requirepass,pass,ok,cancel,clear);
                aiquizcreationparameters.setThemeVariants(DialogVariant.AURA_NO_PADDING);
                aiquizcreationparameters.open();
                requirepass.addValueChangeListener(event->{
                    if(requirepass.getValue().toString().equalsIgnoreCase("yes"))
                        pass.setVisible(true);

                    else {
                        pass.setVisible(false);
                        pass.setValue("");
                    }

                    quizname.setRequiredIndicatorVisible(true);
                    quizname.setRequired(true);
                    quizname.setPlaceholder("Enter the name for the quiz you will create");
                    ok.addClickListener(event1 -> {
                        //String quizname, String time,String totalmarks,String negativemarks,quiz.requirepass requirepass,String pass
                        onSaveListener.save(lastGenerated,quizname.getValue(),timer.getValue(),totalmarks.getValue(),negativemarks.getValue(),
                                requirepass.getValue(),pass.getValue());
                        notify("Questions saved successfully! and Quiz has been Created,Check your profile inventory", NotificationVariant.LUMO_SUCCESS);
                        aiquizcreationparameters.close();
                    });
                    cancel.addClickListener(event1 -> {aiquizcreationparameters.close();});
                });
                aiquizcreationparameters.add(fieldslayout);
            } else {
                notify("No save handler configured.", NotificationVariant.LUMO_WARNING);
            }
        });

        HorizontalLayout actionBar = new HorizontalLayout(countLabel, toggleAnswers, regenerateBtn, saveAllBtn);
        actionBar.setWidthFull();
        actionBar.setAlignItems(Alignment.CENTER);
        actionBar.setFlexGrow(1, countLabel);

        // Stats chips
        HorizontalLayout stats = new HorizontalLayout(
                chip("Topic",      params.topic()),
                chip("Category",   params.category()),
                chip("Difficulty", params.difficulty().name()),
                chip("Type",       params.questionCategory() == question.questionategory.SingleAndMultipleAnswers
                        ? "Single and Multiple Answer" :
                        "Multiple Answers")
        );
        stats.setSpacing(true);
        stats.setPadding(false);

        resultsSection.add(actionBar, stats);

        for (int i = 0; i < questions.size(); i++) {
            resultsSection.add(buildQuestionCard(questions.get(i), i + 1));
        }
    }

    private Div buildQuestionCard(question q, int index) {
        Div card = new Div();
        styleInline(card,
                "background:#111118;border:1px solid #2a2a3a;border-radius:16px;" +
                        "padding:22px;margin-bottom:12px");

        // Meta
        Span num  = new Span("Q" + index);
        styleInline(num,
                "background:rgba(124,106,255,.12);color:#7c6aff;font-size:11px;" +
                        "letter-spacing:.08em;padding:3px 10px;border-radius:100px");

        Span type = new Span(q.getQuestioncategory() == question.questionategory.SingleAnswer
                ? "SINGLE ANSWER" : "MULTIPLE ANSWERS");
        styleInline(type, "color:#7070a0;font-size:11px;letter-spacing:.1em;margin-left:8px");

        Div meta = new Div(num, type);
        styleInline(meta, "margin-bottom:12px");

        // Question title
        Paragraph qTitle = new Paragraph(q.getQuestion_title());
        styleInline(qTitle,
                "font-size:15px;font-weight:600;line-height:1.5;" +
                        "margin-bottom:14px;color:#e8e8f0");

        card.add(meta, qTitle);

        // Options
        String[] options  = { q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4() };
        String[] labels   = { "A", "B", "C", "D" };
        String   correct  = q.getRight_answer() != null ? q.getRight_answer() : "";

        for (int i = 0; i < options.length; i++) {
            if (options[i] == null || options[i].isBlank()) continue;

            // For MultipleAnswers, right_answer is comma-separated option texts
            int finalI = i;
            boolean isCorrect = showAnswers.get() && (
                    q.getQuestioncategory() == question.questionategory.MultipleAnswers
                            ? List.of(correct.split(",")).stream()
                            .map(String::trim)
                            .anyMatch(c -> c.equalsIgnoreCase(options[finalI].trim()))
                            : correct.trim().equalsIgnoreCase(options[i].trim())
            );

            Div optDiv = new Div();
            styleInline(optDiv,
                    "display:flex;align-items:flex-start;gap:12px;padding:9px 14px;" +
                            "border-radius:10px;font-size:13px;font-family:monospace;" +
                            "margin-bottom:6px;transition:background .2s;" +
                            (isCorrect
                                    ? "background:rgba(106,255,218,.07);border:1px solid rgba(106,255,218,.25);color:#6affda"
                                    : "background:#1a1a24;border:1px solid transparent;color:#e8e8f0")
            );

            Span key = new Span(labels[i]);
            styleInline(key,
                    "font-weight:600;min-width:18px;" +
                            (isCorrect ? "color:#6affda" : "color:#7070a0"));

            Span text = new Span(options[i]);

            optDiv.add(key, text);

            if (isCorrect) {
                Span mark = new Span("✓ correct");
                styleInline(mark,
                        "margin-left:auto;font-size:10px;letter-spacing:.1em;" +
                                "text-transform:uppercase;opacity:.7");
                optDiv.add(mark);
            }

            card.add(optDiv);
        }

        // Show correct answer text for MultipleAnswers
        if (showAnswers.get()
                && q.getQuestioncategory() == question.questionategory.MultipleAnswers
                && correct != null && !correct.isBlank()) {
            Div answerDiv = new Div();
            styleInline(answerDiv,
                    "margin-top:10px;padding:10px 14px;border-radius:10px;" +
                            "background:rgba(106,255,218,.07);border:1px solid rgba(106,255,218,.2);" +
                            "font-size:12px;font-family:monospace;color:#6affda");
            Span label = new Span("CORRECT: ");
            styleInline(label, "color:#7070a0;font-size:10px;letter-spacing:.1em;text-transform:uppercase");
            answerDiv.add(label, new Span(correct));
            card.add(answerDiv);
        }

        return card;
    }

    // ── Helpers ────────────────────────────────────────────────────────────────

    private static HorizontalLayout row(com.vaadin.flow.component.Component... components) {
        HorizontalLayout hl = new HorizontalLayout(components);
        hl.setWidthFull();
        for (var c : components) hl.setFlexGrow(1, c);
        hl.setSpacing(true);
        hl.setPadding(false);
        return hl;
    }

    private static Span sectionLabel(String text) {
        Span s = new Span(text.toUpperCase());
        styleInline(s,
                "font-size:10px;letter-spacing:.18em;color:#7070a0;" +
                        "display:block;margin-bottom:2px");
        return s;
    }

    private static Div chip(String label, String value) {
        Div d = new Div();
        styleInline(d,
                "background:#1a1a24;border:1px solid #2a2a3a;border-radius:10px;" +
                        "padding:7px 14px;font-size:12px;font-family:monospace;color:#7070a0");
        Span v = new Span(value);
        styleInline(v, "color:#e8e8f0;font-weight:500;margin-left:4px");
        d.add(new Span(label + ":"), v);
        return d;
    }

    private static void styleSecondaryBtn(Button btn) {
        // clear previous listeners by removing theme variants then re-styling
        btn.removeThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        styleInline(btn,
                "background:transparent;border:1px solid #2a2a3a;color:#e8e8f0;" +
                        "border-radius:10px;font-weight:600;cursor:pointer;padding:8px 16px");
    }

    private static void applyFieldTheme(com.vaadin.flow.component.Component c) {
        c.getElement().getStyle()
                .set("--vaadin-input-field-background",    "#1a1a24")
                .set("--vaadin-input-field-border-color",  "#2a2a3a")
                .set("--vaadin-input-field-color",         "#e8e8f0")
                .set("--vaadin-input-field-label-color",   "#7070a0")
                .set("--vaadin-focus-ring-color",          "#7c6aff")
                .set("border-radius",                      "12px");
        c.getElement().getClassList().add("ai-field");
    }

    private static void styleInline(com.vaadin.flow.component.Component c, String css) {
        for (String rule : css.split(";")) {
            String[] kv = rule.split(":", 2);
            if (kv.length == 2)
                c.getElement().getStyle().set(kv[0].trim(), kv[1].trim());
        }
    }

    private static void notify(String msg, NotificationVariant variant) {
        Notification n = Notification.show(msg, 4000, Notification.Position.MIDDLE);
        n.addThemeVariants(variant);
    }

    private void injectGoogleFont() {
        getElement().executeJs(
                "if(!document.getElementById('ai-quiz-font')){" +
                        "const l=document.createElement('link');" +
                        "l.id='ai-quiz-font';l.rel='stylesheet';" +
                        "l.href='https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700;800&display=swap';" +
                        "document.head.appendChild(l);}");
    }
}
