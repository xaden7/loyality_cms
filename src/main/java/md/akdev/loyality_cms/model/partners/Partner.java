package md.akdev.loyality_cms.model.partners;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Table(name="partners")
@Entity
@Getter
@Setter
public class Partner {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String titleRo; //1
    private String titleRu;
    private String textRo; //2
    private String textRu;
    private String bannerDescriptionRo; //3
    private String bannerDescriptionRu;
    private String shortDescriptionRo; //4
    private String shortDescriptionRu;
    private String veryShortDescriptionRo; //5
    private String veryShortDescriptionRu;

    private int discount;
    @Column(name = "active")
    private boolean active;

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PartnerImage> images = new ArrayList<>();

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PartnerDetailsRu> detailsRu = new ArrayList<>();

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PartnerDetailsRo> detailsRo = new ArrayList<>();

    @OneToMany(mappedBy = "partner", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    private List<PartnerContacts> contacts = new ArrayList<>();

    private boolean showCabinetButton;

    private String loginLink;


    public void setImages(List<PartnerImage> images) {
        this.images =
            images.stream().peek(image -> image.setPartner(this)).toList();
    }

    public void setDetailsRu(List<PartnerDetailsRu> details) {
        this.detailsRu =
                details.stream().peek(detail -> detail.setPartner(this)).toList();
    }

    public void setDetailsRo(List<PartnerDetailsRo> details) {
        this.detailsRo =
                details.stream().peek(detail -> detail.setPartner(this)).toList();
    }

    public void setContacts(List<PartnerContacts> contacts) {
        this.contacts =
            contacts.stream().peek(contact -> contact.setPartner(this)).toList();
    }
}
