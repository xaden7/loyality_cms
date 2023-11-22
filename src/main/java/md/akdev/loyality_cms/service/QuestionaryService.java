package md.akdev.loyality_cms.service;

import md.akdev.loyality_cms.model.ClientsModel;
import md.akdev.loyality_cms.model.JwtAuthentication;
import md.akdev.loyality_cms.model.QuestionaryModel;
import md.akdev.loyality_cms.repository.ClientsRepository;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.support.BasicAuthenticationInterceptor;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import java.util.UUID;

@Service
public class QuestionaryService {
    @Value("${spring.datasource1c.username}") String userName;
    @Value("${spring.datasource1c.password}") String password;
    @Value("${spring.datasource1c.url.getQuestionary}") String urlGetQuestionary;
    @Value("${spring.datasource1c.url.postQuestionary}") String urlPostQuestionary;

    private final JwtAuthService jwtAuthService;
    private final ClientsRepository clientsRepository;
    final RestTemplate restTemplate = new RestTemplate();
    public QuestionaryService(JwtAuthService jwtAuthService, ClientsRepository clientsRepository) {
        this.jwtAuthService = jwtAuthService;
        this.clientsRepository = clientsRepository;
    }

    public QuestionaryModel getQuestionary() throws Exception {
        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getClientByUuid1c(authentication.getUuid());
        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try {
            QuestionaryModel questionaryModel = restTemplate.getForObject(urlGetQuestionary, QuestionaryModel.class, clientsModel.getUuid1c());
            return questionaryModel;
        }catch (Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }
    }

    public QuestionaryModel postQuestionary(@NotNull QuestionaryModel questionaryModel) throws Exception {

        final JwtAuthentication authentication = jwtAuthService.getAuthInfo();
        ClientsModel clientsModel = clientsRepository.getById(UUID.fromString(authentication.getUuid()));
        clientsModel.setClientName(questionaryModel.getName() + questionaryModel.getFirstName());
        clientsModel.setPhoneNumber(questionaryModel.getPhoneNumber());

        restTemplate.getInterceptors().add(new BasicAuthenticationInterceptor(userName, password));
        try{
            QuestionaryModel postQuestionaryModel = restTemplate.postForObject(urlPostQuestionary, questionaryModel, QuestionaryModel.class, clientsModel.getUuid1c());
            return postQuestionaryModel;
        }catch (Exception e){
            throw new Exception(((HttpClientErrorException.NotFound) e).getResponseBodyAsString());
        }

    }

}
