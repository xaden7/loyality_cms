package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.exception.CustomException;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.repository.BonusRepository;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.DeleteRequestRepository;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;




import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

@Service
public class ClientService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getBonus}") String urlGetBonus;
    @Value("${spring.datasource1c.url.getBonusByPhone}")
    String urlGetBonusByPhone;
    @Value("${spring.datasource1c.url.newClient}") String urlNewClient;
    @Value("${spring.datasource1c.url.temporaryCode}") String urlTemporaryCode;
    @Value("${spring.datasource1c.url.getBarcode}") String urlGetBarcode;

    private final MappingUtils mappingUtils;
    private final ClientsRepository clientsRepository;
    private final BonusRepository bonusRepository;
    private final RestTemplate restTemplate;

    private final Logger logger = org.slf4j.LoggerFactory.getLogger(ClientService.class);
    private final DeleteRequestRepository deleteRequestRepository;

    public ClientService(ClientsRepository clientsRepository, MappingUtils mappingUtils, BonusRepository bonusRepository, RestTemplate restTemplate,
                         DeleteRequestRepository deleteRequestRepository) {
        this.clientsRepository = clientsRepository;
        this.mappingUtils = mappingUtils;
        this.bonusRepository = bonusRepository;
        this.restTemplate = restTemplate;
        this.deleteRequestRepository = deleteRequestRepository;
    }

    public ClientsModel mapToClientsModel(ClientDeviceDto dto) {
        return mappingUtils.mapToClientsModel(dto);
    }
    public ClientsModel mapQuestionaryToClientsModel(QuestionaryModel questionaryModel){
        return mappingUtils.mapQuestionaryToClientsModel(questionaryModel);
    }

   @Transactional
   public ClientsModel loginByPhoneNumber(String phoneNumber) throws Exception {
        Optional<ClientsModel> getClient = getClientByPhoneNumber(phoneNumber);

        if (getClient.isPresent()) {
            return getClient.get();
        }
        else {
            String urlGetBonus = this.urlGetBonusByPhone;

            try {
                //AK было начало
                //ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phoneNumber);
                //АК было конец
                //АК Стало начало
                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phoneNumber);
                assert getClientLoyality != null;
                getClientLoyality.setPhoneNumber(phoneNumber);
                //АК Стало конец

                if (getClientLoyality.getUuid1c() == null ){
                    throw new NotFoundException("Client not found in 1c");
                }

                if (clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c()).isPresent() && Objects.equals(clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c()).get().getPhoneNumber(), phoneNumber)) {
                    return getClientLoyality;
                }

                clientsRepository.save(getClientLoyality);
                    return clientsRepository.getClientByPhoneNumber(phoneNumber).orElseThrow(NotFoundException::new);

            }catch (NotFoundException e)
            {
                logger.warn("Client not found in 1c");
                throw new NotFoundException(e.getMessage());
            }
            catch (Exception e) {
                logger.error(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }

}

@Transactional
    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String urlGetBonus = this.urlGetBonus;

        String phone = inputClient.getPhoneNumber();

        phone = phoneDefaultIfNull(phone);

      if (phone.length() != 8) {
            throw new CustomException("Numarul de telefon nu este valid");
       }

       // phone = phone.substring(phone.length()-8);
        String barcode = inputClient.getCodeCard();

        Optional<ClientsModel> getClient = getClientByPhoneNumber(phone);

        if (getClient.isPresent()) {
            if(!Objects.equals(getClient.get().getCodeCard(), barcode)){
                throw new CustomException("Ati activat deja aplicația pentru un alt Card Frumos! Utilizati optiunea \"Am uitat cardul\"");//: " +getClient.getCodeCard());
            } else
                return getClient.get();
        }
        else {
            try {

                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);

                if (getClientLoyality == null || getClientLoyality.getUuid1c() == null ) throw new NotFoundException("Client not found in 1c");

                if (clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c()).isPresent() && Objects.equals(clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c()).get().getPhoneNumber(), phone)) {
                    return getClientLoyality;
                }

                getClientLoyality.setPhoneNumber(phone);
                getClientLoyality.setCodeCard(barcode);


                clientsRepository.save(getClientLoyality);
                return clientsRepository.getClientByPhoneNumber(phone).orElseThrow(NotFoundException::new);
            }
            catch(NotFoundException e)
            {
                logger.warn("Client not found in 1c");
                throw new NotFoundException(e.getMessage());
            }
            catch (DataIntegrityViolationException e) {
                logger.error(e.getMessage());
                throw new DataIntegrityViolationException("Acest card este deja activat pe alt numar de telefon");
            }
            catch (Exception e) {
                logger.error(e.getMessage());
                throw new Exception(e.getMessage());
            }
        }
        /*
        ClientsModel getClient = getClientByPhoneNumber(phone).orElse(null);
        if (getClient == null) {
            try {

                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);
                if(clientsRepository.getClientByUuid1c(getClientLoyality != null ? getClientLoyality.getUuid1c() : null) != null)
                {
                    assert getClientLoyality != null;
                    getClientLoyality = clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c());
                }
                assert getClientLoyality != null;
                getClientLoyality.setPhoneNumber(phone);
                getClientLoyality.setCodeCard(barcode);

                return addClient(getClientLoyality);
            }
            catch(Exception e)
            {
                throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
            }
        }
        else if(!Objects.equals(getClient.getCodeCard(), barcode)){
            throw new CustomException("Ați activat deja aplicația pentru un alt Card Frumos! Utilizati optiunea \"Am uitat cardul\"");//: " +getClient.getCodeCard());
        } else
            return getClient;
    }
    */
    }

    public Optional<ClientsModel> getClientByPhoneNumber(String phoneNumber){
        return clientsRepository.getClientByPhoneNumber(phoneNumber);
    }


    public void addClient (ClientsModel inputClient){
        clientsRepository.save(inputClient);
    }

    public QuestionaryModel newClient(QuestionaryModel questionaryModel) throws Exception{
        System.out.println("newClient" + questionaryModel.toString());

        String urlNewClient = this.urlNewClient;
        try{
            return restTemplate.postForObject(urlNewClient, questionaryModel, QuestionaryModel.class);
        } catch(Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    public TemporaryCodeModel temporaryCode(String uuid1c) throws Exception{
        String urlTemporaryCode = this.urlTemporaryCode;

        try{
            TemporaryCodeModel newTemporaryCodeModel = new TemporaryCodeModel();
            newTemporaryCodeModel.setCode(getRandomNumber());
            return restTemplate.postForObject(urlTemporaryCode, newTemporaryCodeModel, TemporaryCodeModel.class, uuid1c);
        } catch(Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    private Integer getRandomNumber() {
        int min = 100000;
        int max = 999999;
        return (int) ((Math.random() * (max - min)) + min);
    }

    public void saveBonusFirstLogin(BonusModel bonusModel){
        bonusRepository.save(bonusModel);
    }

    public void addBonusForFirstLogin(ClientsModel inputClient){
        BonusModel bonusModel = bonusRepository.getBonusByClientUidAndTypeBonus(inputClient.getUuid1c(), BonusModel.typeBonus.FIRST_LOGIN).orElse(null);
        if (bonusModel==null) {
            BonusModel addBonusModel = new BonusModel();
            addBonusModel.setClientUid(inputClient.getUuid1c());
            addBonusModel.setTypeBonus(BonusModel.typeBonus.FIRST_LOGIN);
            addBonusModel.setBonus(300);
            addBonusModel.setAccured(false);
            saveBonusFirstLogin(addBonusModel);
        }
    }

    public String getBarcode(String phone) throws Exception {
        String urlGetBarcode = this.urlGetBarcode;
        try{
            GetBarcodeModel getBarcodeModel = restTemplate.getForObject(urlGetBarcode, GetBarcodeModel.class, phone);
            String smsText;
            switch(Objects.requireNonNull(getBarcodeModel).getQtyBarcode()){
                case 1:
                    smsText = "Codul tau Card Frumos asociat cu acest numar de telefon este: " + getBarcodeModel.getLastBarcode();
                    System.out.println(smsText);
                    return smsText;
                case 2:
                    smsText = "Pe acest numar de telefon sunt inregistrate mai multe carduri. Cel mai recent ai utilizat acest Card Frumos cu codul:  \n" + getBarcodeModel.getLastBarcode() +
                            "\n" +
                            " Conform regulamentului, peste 7 zile, restul cardurilor vor fi dezactivate. Info 022323333\n";
                    return smsText;
                default:
                    return "Nu exista carduri legate de acest numar de telefon";
            }
        }catch (Exception e) {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    private static String phoneDefaultIfNull(String phone) {
        phone = phone != null ? phone : "0";
        return phone;
    }

    public void deleteRequest(String clientID) {
        DeleteRequest deleteRequest = new DeleteRequest();
        deleteRequest.setClientId(UUID.fromString(clientID));
        deleteRequestRepository.save(deleteRequest);
    }
}
