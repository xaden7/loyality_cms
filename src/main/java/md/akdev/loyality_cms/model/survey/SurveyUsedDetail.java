package md.akdev.loyality_cms.model.survey;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "survey_used_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SurveyUsedDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "survey_used_id")
    @JsonBackReference
    private SurveyUsed surveyUsed;

    @NotNull
    @Column(name = "question_id", nullable = false)
    private Integer questionId;

    @Column(name = "answer_id")
    private Integer answerId;

    @Column(name = "answer_text", length = Integer.MAX_VALUE)
    private String answerText;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}