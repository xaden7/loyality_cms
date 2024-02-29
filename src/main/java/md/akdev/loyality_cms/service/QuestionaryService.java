package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.QuestionaryRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class QuestionaryService {
//     @Value("${spring.datasource1c.username}") String userName;
//    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getQuestionary}") String urlGetQuestionary;
    @Value("${spring.datasource1c.url.updateQuestionary}") String urlUpdateQuestionary;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;

    private final RestTemplate restTemplate;
    private final JwtAuthService jwtAuthService;
    private final ClientsRepository clientsRepository;
    private final QuestionaryRepository questionaryRepository;

    @Autowired
    public QuestionaryService(RestTemplate restTemplate, JwtAuthService jwtAuthService, ClientsRepository clientsRepository, QuestionaryRepository questionaryRepository) {
        this.restTemplate = restTemplate;
        this.jwtAuthService = jwtAuthService;
        this.clientsRepository = clientsRepository;
        this.questionaryRepository = questionaryRepository;
    }

    public QuestionaryModel getQuestionary() {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();

        QuestionaryModel questionaryModel;
        //try to obtain questionary from local db
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());

        if (clientsModel != null) {
             questionaryModel = questionaryRepository.findByClientId(clientsModel.getId()).orElseGet( () -> getQuestionaryFrom1c(authentication));

            //if questionary is not in local db, save it
           if (questionaryModel.getId() == null && questionaryModel.getName() != null && questionaryModel.getFirstName() != null){
               questionaryModel.setClientId(clientsModel.getId());
               questionaryRepository.save(questionaryModel);
           }
        }
        else {
            questionaryModel = getQuestionaryFrom1c(authentication);
        }

        return questionaryModel;
    }

    public QuestionaryModel updateQuestionary(QuestionaryModel questionaryModel)  {

        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());

        if (NetworkUtils.sourceIsAvailable(ipAddress)) {
            questionaryRepository.findByClientId(clientsModel.getId()).ifPresent( i -> {


                if (!Objects.equals(i.getName(), questionaryModel.getName()) && questionaryModel.getName() != null){
                    i.setName(questionaryModel.getName());
                }

                if (!Objects.equals(i.getFirstName(), questionaryModel.getFirstName()) && questionaryModel.getFirstName() != null){
                    i.setFirstName(questionaryModel.getFirstName());
                }

                if (!Objects.equals(i.getPhoneNumber(), questionaryModel.getPhoneNumber()) && questionaryModel.getPhoneNumber() != null){
                    i.setPhoneNumber(questionaryModel.getPhoneNumber());
                }

                if (!Objects.equals(i.getEmail(), questionaryModel.getEmail()) && questionaryModel.getEmail() != null){
                    i.setEmail(questionaryModel.getEmail());
                }

                if (!Objects.equals(i.getLanguage(), questionaryModel.getLanguage()) && questionaryModel.getLanguage() != null){
                    i.setLanguage(questionaryModel.getLanguage());
                }

                if (!Objects.equals(i.getSex(), questionaryModel.getSex()) && questionaryModel.getSex() != null){
                    i.setSex(questionaryModel.getSex());
                }

                if (!Objects.equals(i.getBirthday(), questionaryModel.getBirthday()) && questionaryModel.getBirthday() != null){
                    i.setBirthday(questionaryModel.getBirthday());
                }

                questionaryRepository.save(i);

                restTemplate.postForObject(urlUpdateQuestionary, questionaryModel, QuestionaryModel.class, authentication.getUuid());

                clientsModel.setClientName(i.getName() + " " + i.getFirstName());
                clientsModel.setPhoneNumber(i.getPhoneNumber());
                clientsRepository.save(clientsModel);
            });
            return questionaryModel;
        }

        return questionaryRepository.findByClientId(clientsModel.getId()).orElse(new QuestionaryModel());
    }


    private QuestionaryModel getQuestionaryFrom1c(JwtAuthentication authentication) {

        if (NetworkUtils.sourceIsAvailable(ipAddress)) {
            return restTemplate.getForObject(urlGetQuestionary, QuestionaryModel.class, authentication.getUuid());
        } else {
            return new QuestionaryModel();
        }

    }


}
