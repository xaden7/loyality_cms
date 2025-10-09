package md.akdev.loyality_cms.dto.survey;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.survey.SurveyContingent;
import md.akdev.loyality_cms.model.survey.SurveyStatus;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;



@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResponse {


    @JsonProperty("id")
    private Integer id;
    @Enumerated(EnumType.STRING)
    @JsonProperty("contingent")
    private SurveyContingent contingent;
    @JsonProperty("date_from")
    private LocalDate dateFrom;
    @JsonProperty("date_to")
    private LocalDate dateTo;
    @Enumerated(EnumType.STRING)
    @JsonProperty("status")
    private SurveyStatus status ;
    @JsonProperty("name_ru")
    private String nameRu;
    @JsonProperty("name_ro")
    private String nameRo;
    @JsonProperty("description_ru")
    private String descriptionRu;
    @JsonProperty("description_ro")
    private String descriptionRo;
    @JsonProperty("bonus_qty")
    private BigDecimal bonusQty;
    @JsonProperty("banner_image_url")
    private String bannerImageUrl;
    @JsonProperty("questions")
    private LinkedHashSet<SurveyQuestionResponse> surveyQuestions = new LinkedHashSet<>();

}
