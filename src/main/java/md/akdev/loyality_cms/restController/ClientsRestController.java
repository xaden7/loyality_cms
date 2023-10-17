package md.akdev.loyality_cms.restController;

import jakarta.validation.Valid;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;


@RestController
public class ClientsRestController {
    @Autowired
    ClientsRepository clientsRepository;

    final RestTemplate restTemplate = new RestTemplate();

    public ClientsModel getClientByPhoneAndBarcode(ClientsModel inputClient){

        String phone = inputClient.getPhoneNumber();
        String barcode = inputClient.getCodeCard();

        ClientsModel getClient = clientsRepository.getClientByPhoneAndBarcode(phone, barcode);
        if (getClient==null) {
         restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor("OnicaIon", "tbND3tkEDVKO"));
           ClientsModel getClientLoyality = restTemplate.getForObject("http://violetta:8010/discount/hs/GetBonus?phone={phone}&barcode={barcode}", ClientsModel.class, phone, barcode);
          getClientLoyality.setPhoneNumber(phone);
          getClientLoyality.setCodeCard(barcode);
          ClientsModel postClient = addClient(getClientLoyality);
          return postClient;
       }
       else
           return getClient;
   }

    @PostMapping("/api/add_client")
   public ClientsModel addClient (@RequestBody @Valid ClientsModel inputClient){
     return clientsRepository.save(inputClient);
   }
}
