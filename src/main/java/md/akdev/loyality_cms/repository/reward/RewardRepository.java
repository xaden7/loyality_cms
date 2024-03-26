package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.Reward;
import md.akdev.loyality_cms.model.reward.RewardsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RewardRepository extends JpaRepository<Reward,Integer> {
    @Query("select p from Reward p where ?1 between p.dateFrom and p.dateTo and p.rewardType = ?2")
    List<Reward> findActiveRewards(LocalDate now, RewardsType rewardType);

    @Query("select p from Reward p where ?1 between p.dateFrom and p.dateTo and p.id not in (select r.reward.id from RewardUsed r where r.client.id = ?2)")
    List<Reward> findAllActiveRewards(LocalDate now, UUID clientId);

    @Query("select p from Reward p where ?1 between p.dateFrom and p.dateTo")
    List<Reward> findAllActiveRewardsNotAuth(LocalDate now);

    @Query( value = "select * from rewards where ( :dateFrom between date_from and date_to or :dateTo between date_from and date_to ) and reward_type = :rewardType", nativeQuery = true)
    List<Reward> findSomeTypeRewards(@Param("dateFrom") LocalDate dateFrom, @Param("dateTo") LocalDate dateTo, @Param("rewardType") Integer rewardType);
}
