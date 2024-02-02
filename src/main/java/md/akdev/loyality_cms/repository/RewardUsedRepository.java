package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.Reward;
import md.akdev.loyality_cms.model.RewardUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RewardUsedRepository extends JpaRepository<RewardUsed, Integer> {

    Optional<RewardUsed> findByRewardAndClient(Reward reward, ClientsModel client);
}
