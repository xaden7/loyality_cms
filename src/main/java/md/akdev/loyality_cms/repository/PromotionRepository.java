package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Integer> {

    @Query("select p from Promotion p where ?1 between p.startDate and p.endDate")
    List<Promotion> findAllActive(LocalDate now);
}
