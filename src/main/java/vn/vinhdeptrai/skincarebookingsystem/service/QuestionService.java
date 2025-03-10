package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuestionResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuizResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.entity.ServiceCategory;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.QuestionMapper;
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
public class QuestionService {
    QuizRepository quizRepository;
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    ServiceCategoryRepository serviceCategoryRepository;
    private final AnswerRepository answerRepository;

    public List<QuestionResponse> getQuestionList() {
        List<Question> questionList = questionRepository.findAll();
        return questionList.stream().map(questionMapper::toQuestionResponse).collect(Collectors.toList());
    }

    public QuestionResponse getQuestion(int id){
        Question question = questionRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        return questionMapper.toQuestionResponse(question);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse create(QuestionCreationRequest request) {

        if (questionRepository.existsByQuestion(request.getQuestion())) {
            throw new AppException(ErrorCode.QUESTION_EXISTED);
        }

        Question question = questionMapper.toQuestion(request);

        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    public QuestionResponse update(int questionId, QuestionUpdateRequest questionUpdateRequest) {
        //chỉ update nội dung câu hỏi và các answer vì ở quiz sẽ có API add hoặc remove question

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        if (questionRepository.existsByQuestion(questionUpdateRequest.getQuestion())){
            throw new AppException(ErrorCode.QUESTION_EXISTED);
        }

        Set<Answer> answers = new HashSet<>();
        if (!questionUpdateRequest.getAnswerIds().isEmpty()) {
            for(int id : questionUpdateRequest.getAnswerIds()){
                answers.add(answerRepository.findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND)));
            }
        }

        question = Question.builder()
                .question(questionUpdateRequest.getQuestion())
                .answers(answers)
                .build();
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }
//
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public void delete(int quizId) {
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));
//        quizRepository.delete(quiz);
//    }
//
//    @PreAuthorize("hasAnyRole('ADMIN')")
//    public QuizResponse addQuestionsToQuiz(int quizId, AddQuestionsToQuizRequest request) {
//        Quiz quiz = quizRepository.findById(quizId)
//                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));
//
//        Set<Question> questions = new HashSet<>(questionRepository.findAllById(request.getQuestionIds()));
//
//        if (questions.isEmpty()) {
//            throw new AppException(ErrorCode.QUESTION_NOT_FOUND);
//        }
//
//        quiz.getQuestions().addAll(questions);
//        return quizMapper.toQuizResponse(quizRepository.save(quiz));
//    }
}
