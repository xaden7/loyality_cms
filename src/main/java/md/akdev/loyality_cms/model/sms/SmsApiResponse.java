package md.akdev.loyality_cms.model.sms;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
public class SmsApiResponse {

    @JsonProperty("result")
    private List<Result> result;
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Result{
        @JsonProperty("code")
        private String code;
        @JsonProperty("messageId")
        private String messageId;
        @JsonProperty("segmentId")
        private String segmentId;
    }

}
