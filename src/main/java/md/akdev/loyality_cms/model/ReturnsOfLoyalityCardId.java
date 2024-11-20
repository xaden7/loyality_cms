package md.akdev.loyality_cms.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.Hibernate;

import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@Embeddable
public class ReturnsOfLoyalityCardId implements java.io.Serializable {
    private static final long serialVersionUID = -343940689237330072L;
    @NotNull
    @Column(name = "client_id_loyality", nullable = false)
    private UUID clientIdLoyality;

    @Size(max = 255)
    @NotNull
    @Column(name = "card_number", nullable = false)
    private String cardNumber;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ReturnsOfLoyalityCardId entity = (ReturnsOfLoyalityCardId) o;
        return Objects.equals(this.clientIdLoyality, entity.clientIdLoyality) &&
                Objects.equals(this.cardNumber, entity.cardNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientIdLoyality, cardNumber);
    }

    public ReturnsOfLoyalityCardId(String cardNumber, UUID clientIdLoyality) {
        this.cardNumber = cardNumber;
        this.clientIdLoyality = clientIdLoyality;
    }

    public ReturnsOfLoyalityCardId() {
    }
}