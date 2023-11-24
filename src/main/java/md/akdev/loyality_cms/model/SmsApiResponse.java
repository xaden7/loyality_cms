package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class SmsApiResponse {

    @JsonProperty("result")
    private List<Result> result;
    @Data
    public static class Result{
        @JsonProperty("code")
        private String code;
        @JsonProperty("messageId")
        private String messageId;
        @JsonProperty("segmentId")
        private String segmentId;
    }

}
