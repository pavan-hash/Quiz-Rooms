# 🎯 Quiz Rooms – AI Powered Quiz Platform

## 📌 Overview

**Quiz Rooms** is a full-stack web application built using **Spring Boot**, **Vaadin Flow**, and **PostgreSQL**. It enables users to create, manage, and participate in quizzes with both manual and AI-assisted workflows.

The platform focuses on **interactive quiz experiences**, **secure user management**, and **detailed performance tracking**.

---

## 🚀 Core Features

### 🔐 Authentication & Security

* Spring Security based authentication system
* Custom `UserDetailsService` implementation
* Password encryption using **BCrypt**
* Role-based access (**ADMIN / USER**)

---

## 🧠 Quiz Creation

### ✍️ Manual Quiz Creation

* Add custom questions with:

    * Multiple options
    * Single / Multiple correct answers
    * Difficulty levels (EASY, MEDIUM, HARD)
* Minimum **5 questions required**
* Configure:

    * Title
    * Duration
    * Total marks
    * Negative marking
    * Password protection

---

### 🤖 AI Quiz Generation

* Integrated with **Spring AI (ChatClient)**
* Generates structured quiz questions dynamically
* Supports:

    * Topic-based generation
    * Difficulty selection
    * Question type (Single / Multiple answers)
* Automatically converts AI response into database entities

---

## 🎮 Quiz Gameplay

### 🔍 Play Quiz

* Search quiz using **unique quiz hash ID**
* Join quiz room and attempt questions

### ⏱️ Timer-Based Auto Submission

* Each quiz has a fixed duration
* Timer is enforced during gameplay
* **Automatic submission when time expires**

---

## 📊 Scoring System

* Dynamic score calculation:

    * Marks per correct answer
    * Negative marking for wrong answers
* Supports:

    * Single-answer validation
    * Multiple-answer comparison logic
* Stores results for analytics

---

## 👤 User Features

### 📄 Profile Management

* View personal details
* Update password securely

### 📊 Analytics Dashboard

* View:

    * Quizzes created
    * Quizzes attempted
* Performance tracking for each quiz

### 📌 Creator Insights

* View all participants of a quiz
* Analyze:

    * Scores
    * Player details

---

## 🏗️ Architecture Overview

### 🧩 Entities

* **User** – Stores user details and roles
* **Question** – Quiz question with options, difficulty, and category
* **Quiz** – Contains quiz metadata and list of questions
* **QuizPlayedBy** – Tracks quiz attempts and scores
* **UserResponse** – Stores user answers during quiz

---

### ⚙️ Services

* **UserService**

    * User registration and password management
    * Authentication-based user retrieval

* **QuizService**

    * Quiz creation and management
    * Unique quiz ID generation (NanoID)
    * Quiz gameplay and scoring logic

* **QuestionService**

    * Question persistence and retrieval

* **QuizPlayedByService**

    * Tracks quiz attempts and results

* **AiQuizGeneratorService**

    * Generates quiz questions using AI
    * Converts JSON responses into entities

* **MyUserDetailsService**

    * Integrates Spring Security with custom user model

---

## 🧩 Tech Stack

* **Backend:** Spring Boot
* **Frontend:** Vaadin Flow
* **Database:** PostgreSQL
* **ORM:** Spring Data JPA
* **Security:** Spring Security + BCrypt
* **AI Integration:** Spring AI (ChatClient)
* **ID Generation:** NanoID

---

## ⚙️ Setup & Installation

### 1️⃣ Configure PostgreSQL Database

* Install PostgreSQL
* Create a database (e.g., `quiz_rooms`)

---

### 2️⃣ Update `application.properties`

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/quiz_rooms
spring.datasource.username=your_username
spring.datasource.password=your_password

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
# Spring AI - openai config for open router
spring.ai.openai.api-key=YOUR_OPENAI_API_KEY from open router
spring.ai.openai.base-url=https://openrouter.ai/api
spring.ai.openai.chat.options.model=CHAT_MODEL
```

---

### 3️⃣ Run the Application

```bash
git clone <your-repo-url>
cd quiz-rooms
./mvnw spring-boot:run
```

---

### 4️⃣ Access Application

```
http://localhost:8080
```

---

## 🔑 Key Functional Highlights

* Unique quiz identification using **hash ID**
* AI-powered quiz generation with structured output parsing
* Timer-based quiz enforcement
* Secure authentication & encrypted credentials
* Persistent storage using JPA & PostgreSQL
* Real-time UI with Vaadin Flow

---

## 📈 Future Enhancements

* Live multiplayer quiz rooms
* Leaderboards
* Email notifications
* Export results (PDF/CSV)
* Advanced analytics dashboard

---

## 🤝 Contribution

Contributions are welcome! Feel free to fork the repository and submit pull requests.

---

## 📜 License

(Add your license here)

---

## 💡 Summary

Quiz Rooms combines **AI-powered automation**, **robust backend architecture**, and **interactive UI** to deliver a complete quiz platform for both creators and participants.

---
