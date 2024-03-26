package md.akdev.loyality_cms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import md.akdev.loyality_cms.model.Branch;
import md.akdev.loyality_cms.model.transaction.TransactionModel;
import md.akdev.loyality_cms.repository.BranchRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getTransaction}") String urlGetTransaction;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;

    private final BranchRepository branchRepository;
    private final RestTemplate restTemplate;

    public TransactionService(BranchRepository branchRepository, RestTemplate restTemplate) {
        this.branchRepository = branchRepository;
        this.restTemplate = restTemplate;
    }

    public List<TransactionModel> getTransaction() throws Exception {
        try {

            return getTransactionFrom1c();
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

    private List<TransactionModel> getTransactionFrom1c() {

        if (NetworkUtils.sourceIsAvailable(ipAddress)) {
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
            Object[] transaction = restTemplate.getForObject(urlGetTransaction, Object[].class, SecurityContextHolder.getContext().getAuthentication().getPrincipal());
            ObjectMapper mapper = new ObjectMapper();
            assert transaction != null;
            return Arrays.stream(transaction)
                    .map(e -> mapper.convertValue(e, TransactionModel.class))
                    .peek(e -> e.setAddress(pharmaAddress(e.getPharmCode())))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

}
