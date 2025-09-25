package md.akdev.loyality_cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.model.order1c.Order;
import md.akdev.loyality_cms.repository.BranchRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getOrders}") String urlGetOrders;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;

    private final BranchRepository branchRepository;
    private final RestTemplate restTemplate;

    public List<Order> getOrders() throws Exception {
        try {

            return getOrdersFrom1C();
        }
        catch (Exception e)
        {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    private List<Order> getOrdersFrom1C() {

        String ipAddress = this.ipAddress;
        String urlGetTransaction = this.urlGetOrders;

        if (NetworkUtils.sourceIsAvailable(ipAddress, 8010)) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            Object[] orders;
            try {
                orders =   restTemplate.getForObject(urlGetTransaction, Object[].class, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            } catch (HttpClientErrorException e) {
                throw new RuntimeException("Error getting transaction from 1c: " + e.getMessage());
            }
            ObjectMapper mapper = new ObjectMapper();
            // Register the JSR310 module to handle Java 8 date/time types like LocalDateTime
            mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());

            assert orders != null;
            return Arrays.stream(orders)
                    .map(e -> mapper.convertValue(e, Order.class))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
}
