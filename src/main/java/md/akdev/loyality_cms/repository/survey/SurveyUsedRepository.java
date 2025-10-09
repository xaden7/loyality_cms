package md.akdev.loyality_cms.repository.survey;

import md.akdev.loyality_cms.model.survey.SurveyUsed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SurveyUsedRepository extends JpaRepository<SurveyUsed, Integer> {
    boolean existsBySurveyIdAndClientId(Integer id, UUID clientId);
}
