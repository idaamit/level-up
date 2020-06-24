package org.sparkaton.levelup.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Question {
    int questionId;
    String question;
    List<Answer> answers;
    int correctAnswer;
}
