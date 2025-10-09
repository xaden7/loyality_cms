package md.akdev.loyality_cms.dto.survey;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SurveyUsedRequest {
    @JsonProperty("survey_id")
    @NotNull(message = "survey_id is required")
    private Integer surveyId;
    @JsonProperty("question_answers")
    @NotNull(message = "question_answers is required")
    List<QuestionAnswer> questionAnswers;


    @Getter
    @Setter
    public static class QuestionAnswer {
        @JsonProperty("question_id")
        @NotNull(message = "question_id is required")
        private Integer questionId;
        @JsonProperty("answer_id")
        private Integer answerId;
        @JsonProperty("answer_text")
        private String answerText;
    }
}
