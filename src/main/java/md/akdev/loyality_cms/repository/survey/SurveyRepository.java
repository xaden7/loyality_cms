package md.akdev.loyality_cms.repository.survey;

import md.akdev.loyality_cms.model.survey.Survey;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Integer> {

    @Query(value = """
    select sr.* from survey sr
            where sr.status = 'APPROVED' and now()::date between date_from and date_to
            and not exists(
                select 1 from survey_used su where su.survey_id = sr.row_id and su.client_id = :clientId
            )
""", nativeQuery = true)
    List<Survey> findAllActiveSurvey(@Param("clientId") UUID clientId);
}
