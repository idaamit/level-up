package org.sparkaton.levelup;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sparkaton.levelup.DB.DB;
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
    private final QuillionzService quillionzService = new QuillionzService();

    @ResponseStatus(HttpStatus.OK)
    @PostMapping(value = "v1/createquiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> createQuiz(@RequestBody QuizRequest quizRequest) {
        Quiz quiz = quillionzService.createQuiz(quizRequest);
        return new ResponseEntity<>(quiz, HttpStatus.OK);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping(value = "v1/quiz", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Quiz> getQuiz() {
        Quiz quiz = DB.getLastQuiz();
        return new ResponseEntity<Quiz>(
                quiz,
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

}
