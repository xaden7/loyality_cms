package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.DevicesModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface DevicesRepository extends JpaRepository<DevicesModel, String>  {
   @Query(value = "SELECT * FROM DEVICES WHERE Device_Id = ?1 ", nativeQuery = true)
   DevicesModel getDeviceByDeviceId (@Param("DeviceId") String deviceId);

}
