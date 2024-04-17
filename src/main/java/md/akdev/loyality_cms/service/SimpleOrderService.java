package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.SimpleOrderDTO;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.order.SimpleOrder;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.order.SimpleOrderRepository;
import md.akdev.loyality_cms.repository.product.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class SimpleOrderService {
    private final SimpleOrderRepository simpleOrderRepository;
    private final ClientsRepository clientsRepository;
    private final ProductRepository productRepository;


    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void newOrder (SimpleOrderDTO simpleOrderDTO) {

        if (simpleOrderDTO.getProducts().isEmpty()) {
            throw new RuntimeException("Products list is empty");
        }

        SimpleOrder simpleOrder = new SimpleOrder();

        ClientsModel client = clientsRepository.findById(simpleOrderDTO.getClientId())
                .orElseThrow(() -> new RuntimeException("Client not found"));

        simpleOrder.setClient(client);
        simpleOrder.setMovedToLoyalty(0);
        simpleOrder.setSimpleOrdersRows(new ArrayList<>());

        simpleOrderDTO.getProducts().forEach(product -> productRepository.findByArticle(product.getArticle())
                .ifPresentOrElse(prod -> simpleOrder.addProduct(prod, product.getQty(), product.getPrice()), () -> {
                    throw new RuntimeException("Product not found");
                }));
          simpleOrderRepository.save(simpleOrder);
    }


}
