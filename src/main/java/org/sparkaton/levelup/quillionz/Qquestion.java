package org.sparkaton.levelup.quillionz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Qquestion {
    @JsonProperty("question")
    String question;
    @JsonProperty("answer")
    String answer;
    @JsonProperty("correctSent")
    String correctSent;
}
