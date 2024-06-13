package md.akdev.loyality_cms.model.reward;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "rewards_details")
public class RewardDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "reward_id", referencedColumnName = "id")
    @JsonBackReference
    private Reward reward;

    @Size(max = 255)
    @NotNull
    @Column(name = "qr_code", nullable = false)
    private String qrCode;

    @Column(name = "bonus_qty")
    private BigDecimal bonusQty;

    @Column(name = "description")
    private String description;

    @Column(name = "description_ru")
    private String descriptionRu;

    @OneToOne(mappedBy = "rewardDetails", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @JsonManagedReference
    @OnDelete(action = OnDeleteAction.CASCADE)
    private RewardsDetailsMultimedia rewardsDetailsMultimedia;


    @Override
    public String toString() {
        return "RewardDetail{" +
                "id=" + id +
                ", qrCode='" + qrCode + '\'' +
                ", bonusQty=" + bonusQty +
                ", description='" + description + '\'' +
                ", descriptionRu='" + descriptionRu + '\'' +
                '}';
    }
}
