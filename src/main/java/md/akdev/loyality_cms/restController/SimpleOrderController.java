package md.akdev.loyality_cms.restController;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.SimpleOrderDTO;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.service.SimpleOrderService;
import org.slf4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/order")
@RequiredArgsConstructor
public class SimpleOrderController {
    private final SimpleOrderService simpleOrderService;
    private final ClientsRepository clientsRepository;
    private final Logger logger = org.slf4j.LoggerFactory.getLogger(SimpleOrderController.class);

    @PostMapping("/new-order")
    public ResponseEntity<?> newOrder(@RequestBody SimpleOrderDTO simpleOrderDTO){

        if (simpleOrderDTO.getClientId() == null){
            clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).ifPresent(cl -> simpleOrderDTO.setClientId(cl.getId()));
        }
        simpleOrderService.newOrder(simpleOrderDTO);
        return ResponseEntity.ok().body("Order created");
    }
}
