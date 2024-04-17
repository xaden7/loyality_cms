package md.akdev.loyality_cms.repository.product;

import md.akdev.loyality_cms.model.product.ProductForSite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<ProductForSite, UUID>{
    List<ProductForSite> findAllByArticleIn(List<String> article);

    Optional<ProductForSite> findByArticle(String article);
}
