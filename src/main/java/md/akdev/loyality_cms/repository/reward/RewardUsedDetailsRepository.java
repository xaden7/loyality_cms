package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.RewardUsedDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RewardUsedDetailsRepository  extends JpaRepository<RewardUsedDetails, Integer>{
    Optional<Object> findByRewardDetailIdAndClientId(Integer rewardDetail, UUID client);
}
