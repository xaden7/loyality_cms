package md.akdev.loyality_cms.model.order;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import md.akdev.loyality_cms.model.ClientsModel;
import org.hibernate.annotations.*;

import java.time.Instant;
import java.util.List;

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

}