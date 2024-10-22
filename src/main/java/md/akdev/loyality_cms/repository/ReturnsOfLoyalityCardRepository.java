package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.ReturnsOfLoyalityCard;
import md.akdev.loyality_cms.model.ReturnsOfLoyalityCardId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReturnsOfLoyalityCardRepository extends JpaRepository<ReturnsOfLoyalityCard, ReturnsOfLoyalityCardId> {

    @Query(value = """
        select * from returns_of_loyality_cards
        where  client_id_loyality = :clientId and promo_code = :promoCode
        """, nativeQuery = true)
    Optional<ReturnsOfLoyalityCard> findByClientIdAndPromoCode(@Param("clientId") UUID clientId,@Param("promoCode") String promoCode);
}
