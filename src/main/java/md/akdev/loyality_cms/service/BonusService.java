package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class BonusService {
    @Value("${spring.datasource1c.url.getRefreshBonus}") String urlGetRefreshBonus;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;
    private final ClientsRepository clientsRepository;
    private final RestTemplate restTemplate;

    public BonusService(ClientsRepository clientsRepository, RestTemplate restTemplate) {

        this.clientsRepository = clientsRepository;
        this.restTemplate = restTemplate;
    }

    public Optional<ClientsModel> getRefreshBonus() throws InterruptedException {
        Optional<ClientsModel> clientsModel = clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
        Thread.sleep(2000);
        clientsModel.ifPresentOrElse(this::getRefreshBonusFrom1c, () -> {
            throw new RuntimeException("Client " + SecurityContextHolder.getContext().getAuthentication().getPrincipal() +" not found in database " );
        });

        return clientsModel;
        //return clientsModel.map(this::getRefreshBonusFrom1c);
    }

    private void getRefreshBonusFrom1c(ClientsModel clientsModel) {

        String ipAddress = this.ipAddress;
        String urlGetRefreshBonus = this.urlGetRefreshBonus;

        Double bonus ;

        if (NetworkUtils.sourceIsAvailable(ipAddress, 8010)){

            try {
                bonus = Objects.requireNonNull(
                        restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c())).getBonus();
            } catch (HttpStatusCodeException e) {
                throw new RuntimeException("Error getting bonus from 1c: " + e.getMessage());
            }

            clientsModel.setBonus(bonus);
//            clientsModel.setBonus(Objects.requireNonNull(
//                    restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c())).getBonus());
            clientsRepository.save(clientsModel);
        }

    }

}
