package md.akdev.loyality_cms.model.survey;

import lombok.Getter;

@Getter
public enum SurveyType {
    SINGLE_CHOICE("Один вариант ответа"),
    MULTIPLE_CHOICE("Несколько вариантов ответа"),
    STARS("Шкала 1-5"),
    NPS("NPS 0-10"),
    BOOLEAN("Да/Нет"),
    TEXT("Открытый текст"),
    IMAGE_CHOICE("Выбор изображения"),;
    public final String description;
    SurveyType(String description) {
        this.description = description;
    }
}
