package md.akdev.loyality_cms.repository;


import md.akdev.loyality_cms.model.partners.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, UUID> {
}
