package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.RewardsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RewardTypeRepository extends JpaRepository<RewardsType, Integer> {
    @Query(value = """
           select * from rewards_type where id in (select reward_type from rewards where id = :id)
        """, nativeQuery = true)
    Optional<RewardsType> findRewardsTypeByRewardIdNativeQuery(Integer id);

    List<RewardsType> findByRewardMethod(Integer rewardMethod);
}
