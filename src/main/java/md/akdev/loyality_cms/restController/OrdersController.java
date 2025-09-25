package md.akdev.loyality_cms.restController;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.order1c.Order;
import md.akdev.loyality_cms.service.OrderService;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/user")
@RequiredArgsConstructor
public class OrdersController {
    private final OrderService orderService;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(OrdersController.class);



    @GetMapping("getOrders")
    public ResponseEntity<?> getOrders(HttpServletRequest request){
        try
        {
            List<Order> orders = orderService.getOrders();
            logger.info("OrdersController | getOrders: Phone - \u001B[32m"+ request.getUserPrincipal().getName() + "\u001B[0m; " + request.getHeader("Authorization") + " - " + orders.size() + " records");
            return new ResponseEntity<>(orders, HttpStatus.OK);
        }
        catch (Exception e)
        {
            logger.error("OrdersController | getOrders: " + e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
