package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.reward.Reward;
import md.akdev.loyality_cms.model.reward.RewardDetail;
import md.akdev.loyality_cms.model.reward.RewardUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardUsedRepository extends JpaRepository<RewardUsed, Integer> {

    Optional<RewardUsed> findByRewardAndClient(Reward reward, ClientsModel client);

    Optional<RewardUsed> findByRewardDetailAndClient(RewardDetail rewardDetail, ClientsModel client);
}
