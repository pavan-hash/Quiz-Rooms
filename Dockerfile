FROM eclipse-temurin:21-jdk
COPY target/quizrooms.jar /usr/
EXPOSE 8080
ENTRYPOINT  ["java","-jar","usr/quizrooms.jar"]