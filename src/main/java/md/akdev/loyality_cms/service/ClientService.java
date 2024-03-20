package md.akdev.loyality_cms.service;

import jakarta.transaction.Transactional;
import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.exception.CustomException;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.repository.BonusRepository;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;
import java.util.Optional;

@Service
public class ClientService {

    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getBonus}") String urlGetBonus;
    @Value("${spring.datasource1c.url.newClient}") String urlNewClient;
    @Value("${spring.datasource1c.url.temporaryCode}") String urlTemporaryCode;
    @Value("${spring.datasource1c.url.getBarcode}") String urlGetBarcode;

    private final MappingUtils mappingUtils;
    private final ClientsRepository clientsRepository;
    private final BonusRepository bonusRepository;
    private final RestTemplate restTemplate;

    public ClientService(ClientsRepository clientsRepository, MappingUtils mappingUtils, BonusRepository bonusRepository, RestTemplate restTemplate) {
        this.clientsRepository = clientsRepository;
        this.mappingUtils = mappingUtils;
        this.bonusRepository = bonusRepository;
        this.restTemplate = restTemplate;
    }

    public ClientsModel mapToClientsModel(ClientDeviceDto dto) {
        return mappingUtils.mapToClientsModel(dto);
    }
    public ClientsModel mapQuestionaryToClientsModel(QuestionaryModel questionaryModel){
        return mappingUtils.mapQuestionaryToClientsModel(questionaryModel);
    }

    @Transactional
    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String phone = inputClient.getPhoneNumber();
        phone = phone.substring(phone.length()-8);
        String barcode = inputClient.getCodeCard();

        Optional<ClientsModel> getClient = getClientByPhoneNumber(phone);

        if (getClient.isPresent()) {
            if(!Objects.equals(getClient.get().getCodeCard(), barcode)){
                throw new CustomException("Ați activat deja aplicația pentru un alt Card Frumos! Utilizati optiunea \"Am uitat cardul\"");//: " +getClient.getCodeCard());
            } else
                return getClient.get();
        }
        else {
            try {
                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);

//                assert getClientLoyality != null;

                Assert.notNull(getClientLoyality, "Client not found on sever 1c");

                if (clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c()) != null) {
                    return clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c());
                }else {
                    getClientLoyality.setPhoneNumber(phone);
                    getClientLoyality.setCodeCard(barcode);

                   //  addClient(getClientLoyality);
                    clientsRepository.save(getClientLoyality);
                    return getClientLoyality;
                }



//                if(clientsRepository.getClientByUuid1c(getClientLoyality != null ? getClientLoyality.getUuid1c() : null) != null)
//                {
//                    assert getClientLoyality != null;
//                    getClientLoyality = clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c());
//                }
//                assert getClientLoyality != null;
//                getClientLoyality.setPhoneNumber(phone);
//                getClientLoyality.setCodeCard(barcode);
//
//                return addClient(getClientLoyality);
            }
            catch(NotFoundException e)
            {
             //   throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
                throw new NotFoundException("Client not found");
            }
            catch (Exception e){
               throw  new Exception(e.getMessage());
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

      */
    }

    public Optional<ClientsModel> getClientByPhoneNumber(String phoneNumber){
        return clientsRepository.getClientByPhoneNumber(phoneNumber);
    }

    @Transactional
    public ClientsModel addClient (ClientsModel inputClient){
        return clientsRepository.save(inputClient);
    }

    public QuestionaryModel newClient(QuestionaryModel questionaryModel) throws Exception{
        try{
            return restTemplate.postForObject(urlNewClient, questionaryModel, QuestionaryModel.class);
        } catch(Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    public TemporaryCodeModel temporaryCode(String uuid1c) throws Exception{
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
        try{
                GetBarcodeModel getBarcodeModel = restTemplate.getForObject(urlGetBarcode, GetBarcodeModel.class, phone);
                String smsText;
                switch(Objects.requireNonNull(getBarcodeModel).getQtyBarcode()){
                    case 1:
                            smsText = "Codul tǎu Card Frumos asociat cu acest numǎr de telefon este: \n" + getBarcodeModel.getLastBarcode();
                        System.out.println(smsText);
                        return smsText;
                    case 2:
                         smsText = "Pe acest numar de telefon sunt inregistrate mai multe carduri. Cel mai recent ai utilizat acest Card Frumos cu codul: " + getBarcodeModel.getLastBarcode() + "\n" +
                                 "\n" +
                                 " Conform regulamentului, peste 7 zile, restul cardurilor vor fi dezactivate. Info 022323333\n" +
                                 "\n";
                        return smsText;
                    default:
                      return "Nu exista carduri legate de acest numar de telefon";
                }
        }catch (Exception e) {
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

}
