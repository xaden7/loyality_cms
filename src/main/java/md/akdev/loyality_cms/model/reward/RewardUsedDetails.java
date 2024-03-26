package md.akdev.loyality_cms.model.reward;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reward_used_details")
public class RewardUsedDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Id
    private Integer id;

    @Column(name = "client_id", nullable = false)
    private UUID clientId;


    @Column(name = "reward_id", nullable = false)
    private Integer rewardId;

    @Column(name = "reward_detail_id", nullable = false)
    private Integer rewardDetailId;

    @Column(name = "reward_detail_multimedia_id", nullable = false)
    private Integer rewardDetailMultimediaId;

    @Column(name = "reward_detail_multimedia_row_id", nullable = false)
    private Integer rewardDetailMultimediaRowId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

}
