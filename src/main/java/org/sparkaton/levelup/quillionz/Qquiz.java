package org.sparkaton.levelup.quillionz;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@NoArgsConstructor
@Getter
public class Qquiz {
    @JsonProperty("ContentEncoding")
    String contentEncoding;

    @JsonProperty("ContentType")
    String contentType;

    @JsonProperty("Data")
    QData data;
    @JsonProperty("JsonRequestBehavior")
    int jsonRequestBehavior;
    @JsonProperty("MaxJsonLength")
    long maxJsonLength;
    @JsonProperty("RecursionLimit")
    int recursionLimit;
}
