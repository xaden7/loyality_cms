package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "returns_of_loyality_cards")
public class ReturnsOfLoyalityCard {
    @EmbeddedId
    private ReturnsOfLoyalityCardId id;

    @Size(max = 255)
    @Column(name = "pharmacy_name")
    private String pharmacyName;

    @Size(max = 255)
    @Column(name = "pharmacist_name")
    private String pharmacistName;

    @Column(name = "notification_sent")
    private Boolean notificationSent;

    @Size(max = 255)
    @Column(name = "promo_code")
    private String promoCode;

    @Column(name = "promo_code_used")
    private Boolean promoCodeUsed;

    @Column(name = "reg_date")
    private LocalDateTime regDate;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

}