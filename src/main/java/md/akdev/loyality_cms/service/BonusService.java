package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class BonusService {
//    @Value("${spring.datasource1c.username}") String userName;
//    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getRefreshBonus}") String urlGetRefreshBonus;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;
    private final JwtAuthService jwtAuthService;
    private final ClientsRepository clientsRepository;
    private final RestTemplate restTemplate;

    public BonusService(JwtAuthService jwtAuthService, ClientsRepository clientsRepository, RestTemplate restTemplate) {
        this.jwtAuthService = jwtAuthService;
        this.clientsRepository = clientsRepository;
        this.restTemplate = restTemplate;
    }
//    final RestTemplate restTemplate  = new RestTemplate();

    public ClientsModel getRefreshBonus() throws Exception {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());
/*       restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            ClientsModel clientsModel1c = restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c());
            assert clientsModel1c != null;
            clientsModel.setBonus(clientsModel1c.getBonus());
            return clientsModel;
        }
        catch (Exception e)
        {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }*/
        return getRefreshBonusFrom1c(clientsModel);
    }

    private ClientsModel getRefreshBonusFrom1c(ClientsModel clientsModel) {
        if (NetworkUtils.sourceIsAvailable(ipAddress)){
            clientsModel.setBonus(Objects.requireNonNull(restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c())).getBonus());
            clientsRepository.save(clientsModel);
        }
        return clientsModel;

    }

}
