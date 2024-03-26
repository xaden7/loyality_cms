package md.akdev.loyality_cms.repository.reward;

import md.akdev.loyality_cms.model.reward.RewardsDetailsMultimediaRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardDetailMultimediaRowRepository extends JpaRepository<RewardsDetailsMultimediaRow, Integer> {
}
