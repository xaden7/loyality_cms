package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.PromotionDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionDetailsRepository extends JpaRepository<PromotionDetails, Integer> {
    List<PromotionDetails> findByPromotionId(Integer promotionId);
}
