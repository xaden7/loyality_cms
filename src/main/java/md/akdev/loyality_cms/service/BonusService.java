package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@Service
public class BonusService {
    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getRefreshBonus}") String urlGetRefreshBonus;

    private final JwtAuthService jwtAuthService;
    private final ClientsRepository clientsRepository;

    public BonusService(JwtAuthService jwtAuthService, ClientsRepository clientsRepository) {
        this.jwtAuthService = jwtAuthService;
        this.clientsRepository = clientsRepository;
    }
    final RestTemplate restTemplate  = new RestTemplate();

    public ClientsModel getRefreshBonus() throws Exception {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            ClientsModel clientsModel1c = restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c());
            clientsModel.setBonus(clientsModel1c.getBonus());
            return clientsModel;
        }
        catch (Exception e)
        {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

}
