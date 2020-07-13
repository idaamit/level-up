package org.sparkaton.levelup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserQuiz {
    int quizId;
    List<UserAnswer> userAnswers;
}
