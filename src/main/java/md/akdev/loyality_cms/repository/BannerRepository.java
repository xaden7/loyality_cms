package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface BannerRepository extends JpaRepository<Banner, UUID> {
}
