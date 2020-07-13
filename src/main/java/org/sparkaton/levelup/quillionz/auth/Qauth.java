package org.sparkaton.levelup.quillionz.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Qauth {

    @JsonProperty("access_token")
    String access_token;
    @JsonProperty("scope")
    String scope;
    @JsonProperty("token_type")
    String token_type;
    @JsonProperty("expires_in")
    int expires_in;

}
