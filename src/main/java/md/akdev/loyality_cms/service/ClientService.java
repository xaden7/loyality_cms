package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.ClientDeviceDto;
import md.akdev.loyality_cms.exception.CustomException;
import md.akdev.loyality_cms.model.*;
import md.akdev.loyality_cms.repository.BonusRepository;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.utils.MappingUtils;
import org.springframework.beans.factory.annotation.Value;
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
    @Value("${spring.datasource1c.url.temporaryCode}") String urlTemporaryCode;
    @Value("${spring.datasource1c.url.getBarcode}") String urlGetBarcode;

    final RestTemplate restTemplate = new RestTemplate();
    private final MappingUtils mappingUtils;
    private final ClientsRepository clientsRepository;
    private final BonusRepository bonusRepository;

    public ClientService(ClientsRepository clientsRepository, MappingUtils mappingUtils, BonusRepository bonusRepository) {
        this.clientsRepository = clientsRepository;
        this.mappingUtils = mappingUtils;
        this.bonusRepository = bonusRepository;
    }

    public ClientsModel mapToClientsModel(ClientDeviceDto dto) {
        return mappingUtils.mapToClientsModel(dto);
    }
    public ClientsModel mapQuestionaryToClientsModel(QuestionaryModel questionaryModel){
        return mappingUtils.mapQuestionaryToClientsModel(questionaryModel);
    }

    public ClientsModel getClientByPhoneNumberAndCodeCard(ClientsModel inputClient) throws Exception {

        String phone = inputClient.getPhoneNumber();
        phone = phone.substring(phone.length()-8);
        String barcode = inputClient.getCodeCard();

        //ClientsModel getClient = clientsRepository.getClientByPhoneNumberAndCodeCard(phone, barcode).orElse(null);
        ClientsModel getClient = getClientByPhoneNumber(phone).orElse(null);
        if (getClient == null) {
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
            try {

                ClientsModel getClientLoyality = restTemplate.getForObject(urlGetBonus, ClientsModel.class, phone, barcode);
                if(clientsRepository.getClientByUuid1c(getClientLoyality != null ? getClientLoyality.getUuid1c() : null) != null)
                {
                    getClientLoyality = clientsRepository.getClientByUuid1c(getClientLoyality.getUuid1c());
                }
                getClientLoyality.setPhoneNumber(phone);
                getClientLoyality.setCodeCard(barcode);

                return addClient(getClientLoyality);
            }
            catch(Exception e)
            {
                throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
            }
        }
        else if(getClient.getCodeCard() != barcode){
            throw new CustomException("Ați activat deja aplicația pentru un alt Card Frumos: "+getClient.getCodeCard());
        } else
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
            return restTemplate.postForObject(urlNewClient, questionaryModel, QuestionaryModel.class);
        } catch(Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    public TemporaryCodeModel temporaryCode(String uuid1c) throws Exception{
        try{
            TemporaryCodeModel newTemporaryCodeModel = new TemporaryCodeModel();
            newTemporaryCodeModel.setCode(getRandomNumber());
            restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
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
          //  System.out.println("Keep trying");
                restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
                GetBarcodeModel getBarcodeModel = restTemplate.getForObject(urlGetBarcode, GetBarcodeModel.class, phone);
                String smsText;
                switch(getBarcodeModel.getQtyBarcode()){
                    case 1:
                        // smsText = "De acest numar de telefon este legat Card Frumos cu codul: " + getBarcodeModel.getLastBarcode();
                            smsText = "Codul tǎu Card Frumos asociat cu acest numǎr de telefon este: " + getBarcodeModel.getLastBarcode();
                        //otpraviti soobshenie
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
