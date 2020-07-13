package org.sparkaton.levelup.quillionz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.sparkaton.levelup.dto.Answer;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
public class Qmcq {
    @JsonProperty("Question")
    String question;
    @JsonProperty("Answer")
    String answer;
    @JsonProperty("Options")
    List<String> options;
    @JsonProperty("originalSentence")
    String originalSentence;
}
