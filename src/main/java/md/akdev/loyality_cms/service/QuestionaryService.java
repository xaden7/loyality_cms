package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.dto.QuestionaryDTO;
import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.QuestionaryRepository;
import md.akdev.loyality_cms.utils.NetworkUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class QuestionaryService {

    @Value("${spring.datasource1c.url.getQuestionary}") String urlGetQuestionary;
    @Value("${spring.datasource1c.url.updateQuestionary}") String urlUpdateQuestionary;
    @Value("${spring.datasource1c.ipAddress}")
    String ipAddress;

    private final RestTemplate restTemplate;
    private final ClientsRepository clientsRepository;
    private final QuestionaryRepository questionaryRepository;

    @Autowired
    public QuestionaryService(RestTemplate restTemplate,  ClientsRepository clientsRepository, QuestionaryRepository questionaryRepository) {
        this.restTemplate = restTemplate;
        this.clientsRepository = clientsRepository;
        this.questionaryRepository = questionaryRepository;
    }

    public QuestionaryDTO getQuestionary() {

        QuestionaryModel questionaryModel = new QuestionaryModel();

        //try to obtain questionary from local db
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (clientsModel != null) {
             questionaryModel = questionaryRepository.findByClientId(clientsModel.getId()).orElseGet( () -> getQuestionaryFrom1c(clientsModel.getUuid1c()));

            //if questionary is not in local db, save it
           if (questionaryModel.getId() == null && questionaryModel.getName() != null && questionaryModel.getFirstName() != null){
               questionaryModel.setClientId(clientsModel.getId());
               questionaryRepository.save(questionaryModel);
           }
        }

        return mapModels(questionaryModel);
    }

    public QuestionaryModel updateQuestionary(QuestionaryModel questionaryModel)  {

        ClientsModel clientsModel = clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

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

                restTemplate.postForObject(urlUpdateQuestionary, questionaryModel, QuestionaryModel.class, clientsModel.getUuid1c());

                clientsModel.setClientName(i.getName() + " " + i.getFirstName());
                clientsModel.setPhoneNumber(i.getPhoneNumber());
                clientsRepository.save(clientsModel);
            });
            return questionaryModel;
        }

        return questionaryRepository.findByClientId(clientsModel.getId()).orElse(new QuestionaryModel());
    }


    private QuestionaryModel getQuestionaryFrom1c(String uuid) {

        if (NetworkUtils.sourceIsAvailable(ipAddress)) {
            return restTemplate.getForObject(urlGetQuestionary, QuestionaryModel.class, uuid);
        } else {
            return new QuestionaryModel();
        }

    }

    private QuestionaryDTO mapModels(QuestionaryModel questionaryModel) {
        QuestionaryDTO questionaryDTO = new QuestionaryDTO();
        questionaryDTO.setId(questionaryModel.getClientId());
        questionaryDTO.setName(questionaryModel.getName());
        questionaryDTO.setFirstName(questionaryModel.getFirstName());
        questionaryDTO.setPhoneNumber(questionaryModel.getPhoneNumber());
        questionaryDTO.setBarcode(questionaryModel.getBarcode());

        questionaryDTO.setEmail(questionaryModel.getEmail());
        questionaryDTO.setLanguage(questionaryModel.getLanguage());
        questionaryDTO.setBirthday(questionaryModel.getBirthday());
        questionaryDTO.setSex(questionaryModel.getSex());
    return questionaryDTO;
    }
}
