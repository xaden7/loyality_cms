package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.ClientsModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ClientsRepository extends JpaRepository<ClientsModel, UUID> {

    @Override
    Optional<ClientsModel> findById(UUID uuid);
    Optional<ClientsModel> getClientByPhoneNumberAndCodeCard(String phoneNumber, String codeCard);
    Optional<ClientsModel> getClientByPhoneNumber(String phoneNumber);
    ClientsModel getClientByUuid1c(String uuid1c);

}
