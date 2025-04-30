package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.NotificationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationUserRepository extends JpaRepository<md.akdev.loyality_cms.model.NotificationUser, Integer> {
    List<NotificationUser> findAllByUserId(UUID userId);

}
