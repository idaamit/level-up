package org.sparkaton.levelup.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserAnswer {
    int questionId;
    int timePassedToAnswer;
    boolean wasAnsweredCorrectly;
}
