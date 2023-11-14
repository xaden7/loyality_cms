package md.akdev.loyality_cms.restController;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;


@RestController
public class ClientsRestController {
    @Autowired
    ClientsRepository clientsRepository;
    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getBonus}") String urlGetBonus;

    final RestTemplate restTemplate = new RestTemplate();

    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String phone = inputClient.getPhoneNumber();
        String barcode = inputClient.getCodeCard();

        ClientsModel getClient = clientsRepository.getClientByPhoneNumberAndCodeCard(phone, barcode).orElse(null);
        if (getClient == null) {
         restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);
            //System.out.println("getClientLoyality = " + getClientLoyality + " phone = " + phone + " barcode = " + barcode);
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
