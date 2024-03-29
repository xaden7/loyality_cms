package md.akdev.loyality_cms.repository;

import md.akdev.loyality_cms.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Integer> {
    List<Tag> findAllByPromotionId(Integer id);
}
