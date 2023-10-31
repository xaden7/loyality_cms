package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@RestController
public class ClientsRestController {
    @Autowired
    ClientsRepository clientsRepository;

    final RestTemplate restTemplate = new RestTemplate();

    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String phone = inputClient.getPhoneNumber();
        String barcode = inputClient.getCodeCard();

        ClientsModel getClient = clientsRepository.getClientByPhoneNumberAndCodeCard(phone, barcode).orElse(null);
        if (getClient == null) {
         restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("OnicaIon", "tbND3tkEDVKO"));
        try {
            ClientsModel getClientLoyality = restTemplate.getForObject("http://violetta:8010/discount/hs/GetBonus?phone={phone}&barcode={barcode}", ClientsModel.class, phone, barcode);
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


}
