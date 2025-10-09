package md.akdev.loyality_cms.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.survey.SurveyType;

import java.util.Set;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyQuestionResponse {

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("order_num")
    private Integer orderNum;

    @Enumerated(EnumType.STRING)
    @JsonProperty("type")
    private SurveyType type;

    @JsonProperty("question_text_ru")
    private String questionTextRu;

    @JsonProperty("question_text_ro")
    private String questionTextRo;

    @JsonProperty("required")
    private Boolean required;

    @JsonProperty("survey_answers_options")
    private Set<SurveyAnswerOptionsResponse> surveyAnswersOptions;
}
