package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;

import vn.vinhdeptrai.skincarebookingsystem.dto.request.CalculateRecommendationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.ServiceRecommendationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ServiceRecommendationResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceRecommendation;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.ServiceRecommendationMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuizRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceRecommendationRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.AnswerRepository;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServiceRecommendationService {
    QuizRepository quizRepository;
    ServiceRepository serviceRepository;
    ServiceRecommendationRepository serviceRecommendationRepository;
    ServiceRecommendationMapper serviceRecommendationMapper;
    AnswerRepository answerRepository;

   public List<ServiceRecommendationResponse> getServiceRecommendationList() {
       List<ServiceRecommendation> serviceRecommendationList = serviceRecommendationRepository.findAll();
       return serviceRecommendationList.stream()
            .map(serviceRecommendationMapper::toServiceRecommendationResponse)
            .collect(Collectors.toList());
   }

   public ServiceRecommendationResponse getServiceRecommendation(int id){
       ServiceRecommendation serviceRecommendation = serviceRecommendationRepository.findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.SERVICE_RECOMMENDATION_NOT_FOUND));
       return serviceRecommendationMapper.toServiceRecommendationResponse(serviceRecommendation);
   }

   @PreAuthorize("hasRole('ADMIN')")
   public ServiceRecommendationResponse create(ServiceRecommendationRequest request) {

       Quiz quiz = quizRepository.findById(request.getQuizId())
            .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        vn.vinhdeptrai.skincarebookingsystem.entity.Service
            service = serviceRepository.findById(request.getServiceId())
        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

        if (serviceRecommendationRepository.existsByQuizAndService(quiz, service)) {
            throw new AppException(ErrorCode.SERVICE_RECOMMENDATION_EXISTED);
        }

        ServiceRecommendation serviceRecommendation = ServiceRecommendation.builder()
            .quiz(quiz)
            .service(service)
            .minScore(request.getMinScore())
            .maxScore(request.getMaxScore())
            .build();

        return serviceRecommendationMapper.toServiceRecommendationResponse(serviceRecommendationRepository.save(serviceRecommendation));
   }

   @PreAuthorize("hasRole('ADMIN')")
   public ServiceRecommendationResponse update(int serviceRecommendationId, ServiceRecommendationRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
            .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        vn.vinhdeptrai.skincarebookingsystem.entity.Service
            service = serviceRepository.findById(request.getServiceId())
        .orElseThrow(() -> new AppException(ErrorCode.SERVICE_NOT_FOUND));

       ServiceRecommendation serviceRecommendation = serviceRecommendationRepository.findById(serviceRecommendationId)
            .orElseThrow(() -> new AppException(ErrorCode.SERVICE_RECOMMENDATION_NOT_FOUND));

       serviceRecommendation = ServiceRecommendation.builder()
            .quiz(quiz)
            .service(service)
            .minScore(request.getMinScore())
            .maxScore(request.getMaxScore())
            .build();
            
        return serviceRecommendationMapper.toServiceRecommendationResponse(serviceRecommendationRepository.save(serviceRecommendation));
   }

   @PreAuthorize("hasRole('ADMIN')")
   public void delete(int serviceRecommendationId) {
       ServiceRecommendation serviceRecommendation = serviceRecommendationRepository.findById(serviceRecommendationId)
               .orElseThrow(() -> new AppException(ErrorCode.SERVICE_RECOMMENDATION_NOT_FOUND));
       serviceRecommendationRepository.delete(serviceRecommendation);
   }

    public Set<ServiceRecommendationResponse> calculateRecommendation(CalculateRecommendationRequest request) {
        Quiz quiz = quizRepository.findById(request.getQuizId())
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        List<Answer> answers = answerRepository.findAllById(request.getAnswersIds());
        int totalScore = 0;
        if (!answers.isEmpty()) {
            for (Answer answer : answers) {
                totalScore += answer.getScore();
            }
        }
        Set<ServiceRecommendation> serviceRecommendations = serviceRecommendationRepository.findByQuizAndMinScoreLessThanEqualAndMaxScoreGreaterThanEqual(quiz, totalScore, totalScore);
        return serviceRecommendations.stream()
            .map(serviceRecommendationMapper::toServiceRecommendationResponse)
            .collect(Collectors.toSet());
    }
}
