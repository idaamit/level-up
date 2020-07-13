package org.sparkaton.levelup.quillionz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class QData {
    @JsonProperty("multipleChoiceQuestions")
    QmultipleChoiceQuestion multipleChoiceQuestions;
}
