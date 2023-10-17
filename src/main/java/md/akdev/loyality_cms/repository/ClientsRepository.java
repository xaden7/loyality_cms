package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.ClientsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface ClientsRepository extends JpaRepository<ClientsModel, UUID> {

    @Query(value = "SELECT * FROM CLIENTS WHERE PHONE_NUMBER = ?1 AND CODE_CARD = ?2", nativeQuery = true)
    ClientsModel getClientByPhoneAndBarcode(@Param("phone_number") String name, @Param("barcode") String barcode);

}
