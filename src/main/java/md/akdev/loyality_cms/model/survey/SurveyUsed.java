package md.akdev.loyality_cms.model.survey;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "survey_used")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "row_id", nullable = false)
    private Integer id;

    @NotNull
    @Column(name = "client_id", nullable = false)
    private UUID clientId;

    @NotNull
    @Column(name = "survey_id", nullable = false)
    private Integer surveyId;

    @NotNull
    @Column(name = "send_to_loyality", nullable = false)
    private Boolean sendToLoyality = false;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "surveyUsed", cascade = CascadeType.ALL)
    @JsonManagedReference
    private Set<SurveyUsedDetail> surveyUsedDetails = new LinkedHashSet<>();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

}