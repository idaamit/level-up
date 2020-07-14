package org.sparkaton.levelup.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserStatistics {
    int userId;
    int points;
    int numberOfAnsweredQuizzes;
    int totalNumberOfCorrectAnswers;
    int totalNumberOfWrongAnswers;
}
