package md.akdev.loyality_cms.model.reward;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "rewards_details_multimedia_rows")
public class RewardsDetailsMultimediaRow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reward_details_multimedia_id", referencedColumnName = "id")
    @JsonBackReference
    private RewardsDetailsMultimedia rewardDetailsMultimedia;

    @Size(max = 255)
    @Column(name = "answer")
    private String answer ;

    @Column(name  = "answer_ru")
    private String answerRu;

    @Column(name = "is_correct")
    private Boolean isCorrect = false;

}