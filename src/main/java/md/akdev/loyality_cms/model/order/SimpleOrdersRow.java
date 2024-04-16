package md.akdev.loyality_cms.model.order;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.product.ProductForSite;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import java.math.BigDecimal;


@Getter
@Setter
@Entity
@Table(name = "simple_orders_rows")
public class SimpleOrdersRow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "simple_order_id", referencedColumnName = "id")
    @JsonBackReference
    private SimpleOrder simpleOrder;

    @ManyToOne
    @JoinColumn(name = "prod_id", referencedColumnName = "id")
    @JsonBackReference
    private ProductForSite prod;

    @Column(name = "qty", precision = 10, scale = 3)
    private BigDecimal qty;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;
}