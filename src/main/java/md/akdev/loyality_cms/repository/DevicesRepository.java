package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.DevicesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface DevicesRepository extends JpaRepository<DevicesModel, String>  {

   DevicesModel getDeviceByDeviceId(String deviceId);

}
