package md.akdev.loyality_cms.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyAnswerOptionsResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("answer_text_ru")
    private String answerTextRu;

    @JsonProperty("answer_text_ro")
    private String answerTextRo;

    @JsonProperty("answer_image_url")
    private String answerImageUrl;
}
