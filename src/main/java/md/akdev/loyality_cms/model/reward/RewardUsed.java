package md.akdev.loyality_cms.model.reward;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.ClientsModel;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "reward_used")
public class RewardUsed {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private ClientsModel client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id")
    private Reward reward;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_detail_id")
    private RewardDetail rewardDetail;

    @Column(name = "moved_to_loyality")
    private Integer movedToLoyality;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Override
    public String toString() {

        String clientIdStr = client == null ? "null" : client.getId().toString();
        String rewardIdStr = reward == null ? "null" : reward.getId().toString();

        return "RewardUsed{" +
                "id=" + id +
                ", client=" +  clientIdStr +
                ", reward=" +  rewardIdStr +
                ", movedToLoyality=" + movedToLoyality +
                '}';
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}