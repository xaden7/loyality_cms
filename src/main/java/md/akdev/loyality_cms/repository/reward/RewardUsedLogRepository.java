package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.RewardUsedLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardUsedLogRepository extends JpaRepository<RewardUsedLog, Integer>{
}
