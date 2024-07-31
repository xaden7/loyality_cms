package md.akdev.loyality_cms.model.jwt;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "blocked_tokens")
@NoArgsConstructor
public class BlockedToken {
    @Id
    @Column(name = "token", nullable = false, length = Integer.MAX_VALUE)
    private String token;

    @ColumnDefault("now()")
    @Column(name = "blocked_at")
    private Instant blockedAt;

    public BlockedToken(String token) {
        this.token = token;
        this.blockedAt = Instant.now();
    }
}