package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.partners.PartnerImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PartnerImageRepository extends JpaRepository<PartnerImage, UUID> {
    Optional<PartnerImage> findByPartnerId(UUID partnerId);
}
