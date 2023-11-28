package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ClientService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getBonus}") String urlGetBonus;
    @Value("${spring.datasource1c.url.newClient}") String urlNewClient;

    final RestTemplate restTemplate = new RestTemplate();
    private final MappingUtils mappingUtils;
    private final ClientsRepository clientsRepository;


    public ClientService(ClientsRepository clientsRepository, MappingUtils mappingUtils) {
        this.clientsRepository = clientsRepository;
        this.mappingUtils = mappingUtils;
    }

    public ClientsModel mapToClientsModel(ClientDeviceDto dto) {
        return mappingUtils.mapToClientsModel(dto);
    }


    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String phone = inputClient.getPhoneNumber();
        String barcode = inputClient.getCodeCard();

        ClientsModel getClient = clientsRepository.getClientByPhoneNumberAndCodeCard(phone, barcode).orElse(null);
        if (getClient == null) {
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
            try {

                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);
                getClientLoyality.setPhoneNumber(phone);
                getClientLoyality.setCodeCard(barcode);
                ClientsModel postClient = addClient(getClientLoyality);
                return postClient;
            }
            catch(Exception e)
            {
                throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
            }
        }
        else
            return getClient;
    }

    public Optional<ClientsModel> getClientByPhoneNumber(String phoneNumber){
        return clientsRepository.getClientByPhoneNumber(phoneNumber);
    }

    public ClientsModel addClient (ClientsModel inputClient){
        return clientsRepository.save(inputClient);
    }

    public QuestionaryModel newClient(QuestionaryModel questionaryModel) throws Exception{
        try{
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
            QuestionaryModel newQuestionary = restTemplate.postForObject(urlNewClient, questionaryModel, QuestionaryModel.class);
            return newQuestionary;
        } catch(Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

}
