package md.akdev.loyality_cms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "delete_requests")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeleteRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "client_id")
    private UUID clientId;
    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;
}
