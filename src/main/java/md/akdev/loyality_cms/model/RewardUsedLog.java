package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "reward_used_log")
public class RewardUsedLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "reward_id")
    private Integer rewardId;

    @Column(name = "client_id")
    private UUID clientId;

    @Size(max = 255)
    @Column(name = "operation")
    private String operation;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Override
    public String toString() {

        String clientIdStr = clientId == null ? "null" : clientId.toString();

        return "RewardUsedLog{" +
                "id=" + id +
                ", rewardId=" + rewardId +
                ", clientId=" + clientIdStr +
                '}';
    }
}