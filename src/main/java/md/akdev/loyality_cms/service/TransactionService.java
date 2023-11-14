package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class TransactionService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getTransaction}") String urlGetTransaction;

    private final JwtAuthService jwtAuthService;


    public TransactionService(JwtAuthService jwtAuthService) {
        this.jwtAuthService = jwtAuthService;
    }
    final RestTemplate restTemplate  = new RestTemplate();
    public String getTransaction() throws Exception {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            String transactionModeList = restTemplate.getForObject(urlGetTransaction, String.class, authentication.getUuid());
            return transactionModeList;
        }
            catch (Exception e)
        {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }



}
