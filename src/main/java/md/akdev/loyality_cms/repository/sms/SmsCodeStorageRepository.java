package md.akdev.loyality_cms.repository.sms;

import md.akdev.loyality_cms.model.sms.SmsCodeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsCodeStorageRepository extends JpaRepository<SmsCodeStorage, String> {
   SmsCodeStorage findByPhone(String phone);
}
