package md.akdev.loyality_cms.model.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SmsRequest {
    @JsonProperty("messages")
    @ToString.Exclude
    private List<Message> messages;

    @Setter
    @Getter
    @AllArgsConstructor
    public static
    class Message {
        @JsonProperty("from")
        private String from;
        @JsonProperty("to")
        private String to;
        @JsonProperty("text")
        private String text;
        @JsonProperty("validity")
        private Integer validity;
        @JsonProperty("priority")
        private String priority;
    }


}
