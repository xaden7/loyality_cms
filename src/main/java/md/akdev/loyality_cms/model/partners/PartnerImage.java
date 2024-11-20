package md.akdev.loyality_cms.model.partners;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.ImageType;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "partner_images")
@Getter
@Setter
public class PartnerImage {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private ImageType type;
    private String body;

    @ManyToOne
    @JoinColumn(name = "partner_id", referencedColumnName = "id")
    @JsonIgnore
    private Partner partner;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PartnerImage that = (PartnerImage) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && type == that.type && Objects.equals(partner.getId(), that.partner.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, type, partner.getId());
    }
}
