package md.akdev.loyality_cms.model.survey;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "survey_questions")
public class SurveyQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_id")
    private Survey survey;

    @NotNull
    @Column(name = "order_num", nullable = false)
    private Integer orderNum;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private SurveyType type;

    @NotNull
    @Column(name = "question_text_ru", nullable = false, length = Integer.MAX_VALUE)
    private String questionTextRu;

    @NotNull
    @Column(name = "question_text_ro", nullable = false, length = Integer.MAX_VALUE)
    private String questionTextRo;

    @Column(name = "required")
    private Boolean required;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private SurveyStatus status;

    @OneToMany(mappedBy = "question")
    @JsonManagedReference
    private Set<SurveyAnswersOption> surveyAnswersOptions = new LinkedHashSet<>();

}