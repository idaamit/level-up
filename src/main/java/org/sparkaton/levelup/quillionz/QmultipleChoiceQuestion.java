package org.sparkaton.levelup.quillionz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.List;

@ToString
@NoArgsConstructor
@Getter
public class QmultipleChoiceQuestion {
    @JsonProperty("mcq")
    List<Qmcq> mcq;
    @JsonProperty("trueFalse")
    List<QtrueFalsePerSentence>  trueFalse;
}
