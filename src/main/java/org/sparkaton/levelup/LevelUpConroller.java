package org.sparkaton.levelup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparkaton.levelup.dto.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/level-up/")
public class LevelUpConroller {
    private final Logger logger = LoggerFactory.getLogger(LevelUpConroller.class);

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/q", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getQuillionz() {
        new QuillionzService().getQuestions();
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @ResponseStatus(HttpStatus.OK)
//    @PostMapping(value = "v1/createquize", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Integer> createQuize() {
//        new QuillionzService().getQuestions();
//        return new ResponseEntity<new Integer(81)>(HttpStatus.OK);
//    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/quizzes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> getQuiz(
            @RequestParam(name = "quizType", required = true) int quizType) {
        System.out.println("quizType=" + quizType);
        List<Question> questions = new ArrayList<>();
        addQuestion1(questions);
        addQuestion2(questions);
        addQuestion3(questions);
        return new ResponseEntity<Quiz>(
                Quiz.builder().quizId(81)
                        .title("The name of the quiz")
                        .questions(questions)
                        .build(),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/users/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserStatistics> getUserStatistics(
            @PathVariable int userId) {
        System.out.println("userId=" + userId);
        return new ResponseEntity<UserStatistics>(
                UserStatistics.builder().userId(userId)
                        .points(856)
                        .numberOfAnsweredQuizes(3)
                        .totalNumberOfCorrectAnswers(13)
                        .totalNumberOfWrongAnswers(4)
                        .build(),
                HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "v1/usersQuiz/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity reportUserAnswers(
            @PathVariable int userId,
            @RequestBody UserQuiz userQuiz) {
        System.out.println("userId=" + userId);
        return new ResponseEntity(HttpStatus.OK);
    }

    private void addQuestion1(List<Question> questions) {
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.builder()
                .answerId(8111)
                .answer("It is bad").build());
        answers.add(Answer.builder()
                .answerId(8112)
                .answer("It is good").build());
        answers.add(Answer.builder()
                .answerId(8113)
                .answer("It is very good").build());
        answers.add(Answer.builder()
                .answerId(8114)
                .answer("It is very very good").build());
        questions.add(Question.builder().questionId(811)
                .question("What do you think about the weather?")
                .answers(answers)
                .correctAnswer(8113)
                .build());
    }

    private void addQuestion2(List<Question> questions) {
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.builder()
                .answerId(8121)
                .answer("True").build());
        answers.add(Answer.builder()
                .answerId(8122)
                .answer("False").build());
        questions.add(Question.builder().questionId(812)
                .question("Is it good?")
                .answers(answers)
                .correctAnswer(8122)
                .build());
    }

    private void addQuestion3(List<Question> questions) {
        List<Answer> answers = new ArrayList<>();
        answers.add(Answer.builder()
                .answerId(8131)
                .answer("London").build());
        answers.add(Answer.builder()
                .answerId(8132)
                .answer("Paris").build());
        answers.add(Answer.builder()
                .answerId(8133)
                .answer("NY").build());
        answers.add(Answer.builder()
                .answerId(8134)
                .answer("Rome").build());
        questions.add(Question.builder().questionId(813)
                .question("Where are you going?")
                .answers(answers)
                .correctAnswer(8133)
                .build());
    }
}
