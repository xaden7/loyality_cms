package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.reward.Reward;
import md.akdev.loyality_cms.model.reward.RewardDetail;
import md.akdev.loyality_cms.model.reward.RewardUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RewardUsedRepository extends JpaRepository<RewardUsed, Integer> {

    Optional<RewardUsed> findByRewardAndClient(Reward reward, ClientsModel client);

    Optional<RewardUsed> findByRewardDetailAndClient(RewardDetail rewardDetail, ClientsModel client);

    @Query(value = """
            select * from reward_used where reward_id = :rewardId and client_id = :clientId and cast(created_at as date) = cast(:now as date)
            """, nativeQuery = true)
    Optional<RewardUsed> findByRewardAndClientAndCreatedAt(@Param("rewardId") Integer rewardId, @Param("clientId") UUID clientId, @Param("now") LocalDate now);
}
