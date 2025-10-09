package md.akdev.loyality_cms.model.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "survey_answers_options")
public class SurveyAnswersOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    @JsonBackReference
    private SurveyQuestion question;

    @Column(name = "answer_text_ru", length = Integer.MAX_VALUE)
    private String answerTextRu;

    @Column(name = "answer_text_ro", length = Integer.MAX_VALUE)
    private String answerTextRo;

    @Column(name = "answer_image_url", length = Integer.MAX_VALUE)
    private String answerImageUrl;

}