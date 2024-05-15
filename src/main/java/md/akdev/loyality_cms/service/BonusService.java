package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
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

    public Optional<ClientsModel> getRefreshBonus() {
        Optional<ClientsModel> clientsModel = clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        return clientsModel.map(this::getRefreshBonusFrom1c);
    }

    private ClientsModel getRefreshBonusFrom1c(ClientsModel clientsModel) {

        String ipAddress = this.ipAddress;
        String urlGetRefreshBonus = this.urlGetRefreshBonus;

        if (NetworkUtils.sourceIsAvailable(ipAddress, 8010)){
            clientsModel.setBonus(Objects.requireNonNull(restTemplate.getForObject(urlGetRefreshBonus, ClientsModel.class, clientsModel.getUuid1c())).getBonus());
            clientsRepository.save(clientsModel);
        }
        return clientsModel;

    }

}
