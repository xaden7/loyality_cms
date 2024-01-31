package md.akdev.loyality_cms.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

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
}
