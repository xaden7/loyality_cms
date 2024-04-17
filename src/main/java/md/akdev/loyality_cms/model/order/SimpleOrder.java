package md.akdev.loyality_cms.model.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;

import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.product.ProductForSite;
import org.hibernate.annotations.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "simple_orders")
public class SimpleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private ClientsModel client;

    @ColumnDefault("0")
    @Column(name = "moved_to_loyalty")
    private Integer movedToLoyalty;

    @Column(name = "order_uuid")
    private UUID orderUuid;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "changed_at")
    private Instant changedAt;

    @OneToMany(mappedBy = "simpleOrder", cascade = {CascadeType.ALL}, fetch = FetchType.EAGER)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonManagedReference
    private List<SimpleOrdersRow> simpleOrdersRows ;


    public void addProduct(ProductForSite product, BigDecimal qty, BigDecimal price) {
        SimpleOrdersRow simpleOrdersRow = new SimpleOrdersRow();
        simpleOrdersRow.setSimpleOrder(this);
        simpleOrdersRow.setProd(product);
        simpleOrdersRow.setQty(qty);
        simpleOrdersRow.setPrice(price);

        simpleOrdersRows.add(simpleOrdersRow);
    }
}