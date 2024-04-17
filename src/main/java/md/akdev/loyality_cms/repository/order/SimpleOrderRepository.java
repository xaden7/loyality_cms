package md.akdev.loyality_cms.repository.order;

import md.akdev.loyality_cms.model.order.SimpleOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SimpleOrderRepository extends JpaRepository<SimpleOrder, Integer>{

}
