package md.akdev.loyality_cms.model.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "stocks_all_hashed")
public class StocksAllHashed {
    @Id
    @Column(name = "reg_id", nullable = false)
    private Long id;

    @Column(name = "prod_id")
    private UUID prodId;

    @Column(name = "branch_id")
    private UUID branchId;

    @Column(name = "lot_id")
    private UUID lotId;

    @Column(name = "qty", precision = 10, scale = 3)
    private BigDecimal qty;

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Chisinau'")
    @Column(name = "created_at")
    @UpdateTimestamp
    private Instant createdAt;

    @ColumnDefault("(CURRENT_TIMESTAMP AT TIME ZONE 'Europe/Chisinau'")
    @Column(name = "changed_at")
    @CreationTimestamp
    private Instant changedAt;

}