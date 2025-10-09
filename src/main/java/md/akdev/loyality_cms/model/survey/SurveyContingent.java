package md.akdev.loyality_cms.model.survey;

import lombok.Getter;

@Getter
public enum SurveyContingent {
    ALL("Все пользователи"),
    ;

    public final String description;

    SurveyContingent(String description) {
        this.description = description;
    }
}
