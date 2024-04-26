package md.akdev.loyality_cms.repository.product;

import md.akdev.loyality_cms.model.product.StocksAllHashed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockAllHashedRepository extends JpaRepository<StocksAllHashed, Long>{
}
