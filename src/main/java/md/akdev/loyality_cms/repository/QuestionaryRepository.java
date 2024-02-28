package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.QuestionaryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionaryRepository extends JpaRepository<QuestionaryModel, Integer> {

    Optional<QuestionaryModel> findByClientId(UUID clientId);
}
