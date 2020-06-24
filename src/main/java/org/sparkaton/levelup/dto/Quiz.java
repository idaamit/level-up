package org.sparkaton.levelup.dto;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class Quiz {
    int quizId;
    String title;
    List<Question> questions;
}
