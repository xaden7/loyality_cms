package md.akdev.loyality_cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.akdev.loyality_cms.model.Branch;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.model.TransactionModel;
import md.akdev.loyality_cms.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getTransaction}") String urlGetTransaction;

    private final JwtAuthService jwtAuthService;
    private final BranchRepository branchRepository;
    private final RestTemplate restTemplate;

    public TransactionService(JwtAuthService jwtAuthService, BranchRepository branchRepository, RestTemplate restTemplate) {
        this.jwtAuthService = jwtAuthService;
        this.branchRepository = branchRepository;
        this.restTemplate = restTemplate;
    }
//    final RestTemplate restTemplate  = new RestTemplate();
    public List<TransactionModel> getTransaction() throws Exception {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {

            Object[] transaction = restTemplate.getForObject(urlGetTransaction, Object[].class, authentication.getUuid());
            ObjectMapper mapper = new ObjectMapper();
            assert transaction != null;
            List<TransactionModel> transactionModels = Arrays.stream(transaction)
                    .map(e -> mapper.convertValue(e, TransactionModel.class))
                    .collect(Collectors.toList());

            transactionModels
                    .forEach(e -> e.setAddress(pharmaAddress(e.getPharmCode())));

            return transactionModels;

        }
            catch (Exception e)
        {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    public String pharmaAddress(String pharmaCode){
        Optional<Branch> branchModel = branchRepository.findByCode(pharmaCode);
        return branchModel.isPresent() ? branchModel.get().getAddress() : "";
    }

}
