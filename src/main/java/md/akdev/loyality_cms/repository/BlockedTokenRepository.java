package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.jwt.BlockedToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BlockedTokenRepository extends JpaRepository<BlockedToken, Long> {
    Optional<BlockedToken> findByToken(String token);
}
