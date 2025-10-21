package md.akdev.loyality_cms.service;

import lombok.RequiredArgsConstructor;
import md.akdev.loyality_cms.dto.survey.SurveyResponse;
import md.akdev.loyality_cms.dto.survey.SurveyUsedRequest;
import md.akdev.loyality_cms.exception.NotFoundException;
import md.akdev.loyality_cms.exception.SurveyAlreadyUsedException;
import md.akdev.loyality_cms.model.survey.Survey;
import md.akdev.loyality_cms.model.survey.SurveyUsed;
import md.akdev.loyality_cms.model.survey.SurveyUsedDetail;
import md.akdev.loyality_cms.repository.ClientsRepository;
import md.akdev.loyality_cms.repository.survey.SurveyRepository;
import md.akdev.loyality_cms.repository.survey.SurveyUsedRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyRepository surveyRepository;
    private final ClientsRepository clientsRepository;
    private final SurveyUsedRepository surveyUsedRepository;

    @Transactional
    public List<SurveyResponse> getActiveSurveysForClient() {
        String clientUuid1c = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (Objects.equals(clientUuid1c, "anonymousUser")) {
         throw new NotFoundException("Client object (id key)  is required, please login first");
        }

        UUID clientId =  clientsRepository.getClientByUuid1c(clientUuid1c)
                .orElseThrow(() -> new NotFoundException("Client not found with uuid1c: " + clientUuid1c))
                .getId();

        List<Survey> surveys = surveyRepository.findAllActiveSurvey(clientId);

        return surveys.stream()
                .map(survey -> new ModelMapper().map(survey, SurveyResponse.class))
                .toList();

    }


    @Transactional
    public void saveSurveyUsed(SurveyUsedRequest request) {

        String clientUuid1c = (String) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (Objects.equals(clientUuid1c, "anonymousUser"))
            throw new NotFoundException("Client object (id key)  is required, please login first");


        UUID clientId =  clientsRepository.getClientByUuid1c(clientUuid1c)
                .orElseThrow(() -> new NotFoundException("Client not found with uuid1c: " + clientUuid1c))
                .getId();


        if (request.getSurveyId() == null) {
            throw new NotFoundException("Survey ID is required");
        }

        if (request.getQuestionAnswers().isEmpty()) {
            throw new NotFoundException("Question answers are required");
        }

        request.getQuestionAnswers().forEach(questionAnswer -> {
            if (questionAnswer.getQuestionId() == null) {
                throw new NotFoundException("Question ID is required");
            }
            if (questionAnswer.getAnswerId() == null && (questionAnswer.getAnswerText() == null || questionAnswer.getAnswerText().isEmpty())) {
                throw new NotFoundException("Either answer ID or answer text is required for question ID: " + questionAnswer.getQuestionId());
            }
        });


        Survey survey = surveyRepository.findById(request.getSurveyId()).orElseThrow(
                () -> new NotFoundException("Survey not found with id: " + request.getSurveyId())
        );


        if (surveyUsedRepository.existsBySurveyIdAndClientId(survey.getId(), clientId)) {
            throw new SurveyAlreadyUsedException("Survey already submitted by this client");
        }

        SurveyUsed surveyUsed = SurveyUsed.builder()
                .surveyId(survey.getId())
                .clientId(clientId)
                .sendToLoyality(false)
                .build();

        surveyUsed.setSurveyUsedDetails(
                        request.getQuestionAnswers().stream().map(qa ->
                                SurveyUsedDetail.builder()
                                    .questionId(qa.getQuestionId())
                                    .answerId(qa.getAnswerId())
                                        .surveyUsed(surveyUsed)
                                    .answerText(qa.getAnswerText())
                                        .build())
                                        .collect(java.util.stream.Collectors.toSet())

                );

        surveyUsedRepository.save(surveyUsed);

    }
}
