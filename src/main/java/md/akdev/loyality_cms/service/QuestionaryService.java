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
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

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



        AtomicReference<QuestionaryModel> questionaryModel = new AtomicReference<>(new QuestionaryModel());
        clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).ifPresent( clientsModel -> {

             questionaryModel.set(questionaryRepository.findByClientId(clientsModel.getId()).orElseGet(() -> getQuestionaryFrom1c(clientsModel.getUuid1c())));

            //if questionary is not in local db, save it
            if (questionaryModel.get().getId() == null && questionaryModel.get().getName() != null && questionaryModel.get().getFirstName() != null){
                questionaryModel.get().setClientId(clientsModel.getId());
                questionaryRepository.save(questionaryModel.get());
            }
        }) ;


        return mapModels(questionaryModel.get());
    }

    public QuestionaryModel updateQuestionary(QuestionaryModel questionaryModel)  {

        String ipAddress = this.ipAddress;
        String urlUpdateQuestionary = this.urlUpdateQuestionary;

        Optional<ClientsModel> clientsModel = clientsRepository.getClientByUuid1c((String) SecurityContextHolder.getContext().getAuthentication().getPrincipal());

        if (NetworkUtils.sourceIsAvailable(ipAddress, 8010)) {
            clientsModel.ifPresent( cl ->{

                questionaryRepository.findByClientId(cl.getId()).ifPresent( i -> {


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

                    restTemplate.postForObject(urlUpdateQuestionary, questionaryModel, QuestionaryModel.class, cl.getUuid1c());

                    cl.setClientName(i.getName() + " " + i.getFirstName());
                    cl.setPhoneNumber(i.getPhoneNumber());
                    clientsRepository.save(cl);
                });
            });
            return questionaryModel;
        }


        AtomicReference<QuestionaryModel> questionary = new AtomicReference<>(new QuestionaryModel());

        clientsModel.flatMap(cl -> questionaryRepository.findByClientId(cl.getId())).ifPresent(questionary::set);

        return questionary.get();
    }


    private QuestionaryModel getQuestionaryFrom1c(String uuid) {

        String ipAddress = this.ipAddress;
        String urlGetQuestionary = this.urlGetQuestionary;

        if (NetworkUtils.sourceIsAvailable(ipAddress, 8010)) {
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
