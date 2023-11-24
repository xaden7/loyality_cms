package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.SmsCodeStorage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SmsCodeStorageRepository extends JpaRepository<SmsCodeStorage, String> {
   SmsCodeStorage findByPhone(String phone);
}
