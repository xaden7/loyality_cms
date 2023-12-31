package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PromotionDetailsRepository extends JpaRepository<PromotionDetail, Integer>{
}
