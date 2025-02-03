package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.Reward;
import md.akdev.loyality_cms.model.reward.RewardDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardDetailsRepository extends JpaRepository<RewardDetail, Integer>{

    Optional<RewardDetail> findByRewardAndId(Reward reward, Integer id);

    Optional<RewardDetail> findRewardDetailByRewardAndQrCodeIgnoreCase(Reward reward, String text);
}
