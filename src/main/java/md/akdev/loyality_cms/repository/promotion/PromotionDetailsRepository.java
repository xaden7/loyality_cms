package md.akdev.loyality_cms.repository.promotion;

import md.akdev.loyality_cms.dto.promotion.PromotionDetailImp;
import md.akdev.loyality_cms.model.promotion.PromotionDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PromotionDetailsRepository extends JpaRepository<PromotionDetail, Integer>{

    @Query(value = "select pd.id as id,  pd.image_name as imageName, pd.image_type as imageType" +
            ", pd.product_bonus as productBonus, pd.product_discount as productDiscount," +
            " pd.product_id as productId, pd.product_name as productName, pd.product_price as productPrice," +
            " pd.product_old_price as productOldPrice from promotion_details pd where promotion_id  = ?1", nativeQuery = true)
    List<PromotionDetailImp> findAllByPromotionId(Integer id);
}
