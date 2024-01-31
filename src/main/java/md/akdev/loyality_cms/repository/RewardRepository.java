package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.model.RewardsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Integer> {
    @Query("select p from Reward p where ?1 between p.dateFrom and p.dateFrom and p.rewardType = ?2")
    List<Reward> findActiveRewards(LocalDate now, RewardsType rewardType);

    @Query( value = "select * from rewards where ( :dateFrom between date_from and date_to or :dateTo between date_from and date_to ) and reward_type = :rewardType", nativeQuery = true)
    List<Reward> findSomeTypeRewards(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo, @Param("rewardType") Integer rewardType);
}
