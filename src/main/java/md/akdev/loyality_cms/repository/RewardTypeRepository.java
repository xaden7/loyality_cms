package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.RewardsType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RewardTypeRepository extends JpaRepository<RewardsType, Integer> {
}
