package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AddQuestionsToQuizRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.QuizCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.QuizUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RemoveQuestionsFromQuizRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuizResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.QuizMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AnswerRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuestionRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuizRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.ServiceCategoryRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizService {
    QuizRepository quizRepository;
    QuestionRepository questionRepository;
    QuizMapper quizMapper;
    ServiceCategoryRepository serviceCategoryRepository;
    private final AnswerRepository answerRepository;

    public List<QuizResponse> getQuizList() {
        List<Quiz> quizList = quizRepository.findAll();
        return quizList.stream().map(quizMapper::toQuizResponse).collect(Collectors.toList());
    }

    public QuizResponse getQuiz(int id){
        Quiz quiz = quizRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));
        return quizMapper.toQuizResponse(quiz);
    }

    public QuizResponse getQuizByCategoryID(int categoryId){
        ServiceCategory category = serviceCategoryRepository.findById(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND));
        Quiz quiz = quizRepository.findByCategory_Id(categoryId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));
        return quizMapper.toQuizResponse(quiz);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse create(QuizCreationRequest request) {
        ServiceCategory serviceCategory = serviceCategoryRepository.findById(request.getCate_id())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND));

        if (quizRepository.existsByCategory_Id(request.getCate_id())) {
            throw new AppException(ErrorCode.CATEGORY_ALREADY_EXIST_QUIZ);
        }

        Quiz quiz = Quiz.builder()
                .category(serviceCategory)
                .title(request.getTitle())
                .build();
        quizRepository.save(quiz);
        return quizMapper.toQuizResponse(quiz);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse update(int quizId, QuizUpdateRequest quizUpdateRequest) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        ServiceCategory serviceCategory = serviceCategoryRepository.findById(quizUpdateRequest.getCate_id())
                .orElseThrow(() -> new AppException(ErrorCode.SERVICE_CATE_NOT_FOUND));

        Set<Question> questions = new HashSet<>();
        if (!quizUpdateRequest.getQuestion_Ids().isEmpty()){
            for(int id : quizUpdateRequest.getQuestion_Ids()){
                questions.add(questionRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND)));
            }
        }

        quiz.setCategory(serviceCategory);
        quiz.setTitle(quizUpdateRequest.getTitle());
        quiz.setQuestions(questions);
        return quizMapper.toQuizResponse(quizRepository.save(quiz));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));
        if (!quiz.getQuestions().isEmpty()) {
            for (Question question : quiz.getQuestions()) {
                if (!question.getAnswers().isEmpty()) {
                    answerRepository.deleteAll(question.getAnswers());
                }
                questionRepository.delete(question);
            }
        }
        quizRepository.delete(quiz);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse addQuestionsToQuiz(int quizId, AddQuestionsToQuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        Set<Question> questions = new HashSet<>(questionRepository.findAllById(request.getQuestionIds()));

        if (questions.isEmpty()) {
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
        }

        quiz.getQuestions().addAll(questions);
        return quizMapper.toQuizResponse(quizRepository.save(quiz));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuizResponse removeQuestionsFromQuiz(int quizId, RemoveQuestionsFromQuizRequest request) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        Set<Question> questions = new HashSet<>(questionRepository.findAllById(request.getQuestionIds()));

        if (questions.isEmpty()) {
            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
        }

        quiz.getQuestions().removeAll(questions);
        return quizMapper.toQuizResponse(quizRepository.save(quiz));
    }
}
