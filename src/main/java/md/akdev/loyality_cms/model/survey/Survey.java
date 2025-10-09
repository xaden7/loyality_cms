package md.akdev.loyality_cms.model.survey;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "survey")
public class Survey {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer id;

    @Enumerated(EnumType.STRING)
    @Column(name = "contingent", nullable = false)
    private SurveyContingent contingent ;

    @NotNull
    @Column(name = "date_from", nullable = false)
    private LocalDate dateFrom;

    @NotNull
    @Column(name = "date_to", nullable = false)
    private LocalDate dateTo;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private SurveyStatus status ;

    @Size(max = 255)
    @NotNull
    @Column(name = "name_ru", nullable = false)
    private String nameRu;

    @Size(max = 255)
    @NotNull
    @Column(name = "name_ro", nullable = false)
    private String nameRo;

    @Column(name = "description_ru", length = Integer.MAX_VALUE)
    private String descriptionRu;

    @Column(name = "description_ro", length = Integer.MAX_VALUE)
    private String descriptionRo;

    @Column(name = "banner_image_url", length = Integer.MAX_VALUE)
    private String bannerImageUrl;

    @NotNull
    @Column(name = "bonus_qty", nullable = false, precision = 10, scale = 2)
    private BigDecimal bonusQty;

    @OneToMany(mappedBy = "survey" )
    @JsonManagedReference
    private Set<SurveyQuestion> surveyQuestions = new LinkedHashSet<>();


}