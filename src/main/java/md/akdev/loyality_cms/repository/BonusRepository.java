package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.BonusModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BonusRepository extends JpaRepository<BonusModel, Integer> {

    Optional<BonusModel> findById(Integer id);

    Optional<BonusModel> getBonusByClientUidAndTypeBonus(String clientUid, BonusModel.typeBonus type);

}