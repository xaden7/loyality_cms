package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.SmsCodeLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsCodeLogsRepository extends JpaRepository<SmsCodeLog, Integer> {
}
