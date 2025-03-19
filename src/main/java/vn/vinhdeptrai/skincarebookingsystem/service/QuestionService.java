package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuestionResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.AnswerMapper;
import vn.vinhdeptrai.skincarebookingsystem.mapper.QuestionMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AnswerRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuestionRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuizRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionService {
    QuestionRepository questionRepository;
    QuestionMapper questionMapper;
    AnswerRepository answerRepository;
    private final AnswerMapper answerMapper;
    private final QuizRepository quizRepository;

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

    public QuestionResponse createWithAnswers(QuestionCreationWithAnswersRequest request, int quizID) {
        Quiz quiz = quizRepository.findById(quizID)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        if (questionRepository.existsByQuestion(request.getQuestion())) {
            throw new AppException(ErrorCode.QUESTION_EXISTED);
        }
        Question question = Question.builder()
                .question(request.getQuestion())
                .build();
        question.getQuizzes().add(quiz);
        for (AnswerRequest answerRequest : request.getAnswerRequestList()) {
            question.getAnswers().add(answerMapper.toAnswer(answerRequest));
        }
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse update(int questionId, QuestionUpdateRequest questionUpdateRequest) {
        //chỉ update nội dung câu hỏi và các answer vì ở quiz sẽ có API add hoặc remove question

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        if (questionRepository.existsByQuestion(questionUpdateRequest.getQuestion())){
            throw new AppException(ErrorCode.QUESTION_EXISTED);
        }
        question.setQuestion(questionUpdateRequest.getQuestion());

        for (Answer answerRequest : questionUpdateRequest.getAnswers()){
            Answer answer = answerRepository.findById(answerRequest.getId())
                    .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));

            if (answer.getQuestion().getId() != questionId){
                throw new AppException(ErrorCode.ANSWER_NOT_FOUND);
            }
            answer.setAnswer(answerRequest.getAnswer());
            answer.setScore(answerRequest.getScore());
        }
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        if (!question.getAnswers().isEmpty()) {
            answerRepository.deleteAll(question.getAnswers());
        }
        questionRepository.delete(question);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse addAnswersToQuestion(int questionId, AddAnswersToQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        Set<Answer> answers = new HashSet<>(answerRepository.findAllById(request.getAnswerIds()));

        if (answers.isEmpty()) {
            throw new AppException(ErrorCode.ANSWER_NOT_FOUND);
        }

        question.getAnswers().addAll(answers);
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public QuestionResponse removeAnswersFromQuestion(int questionId, RemoveAnswersFromQuestionRequest request) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        Set<Answer> answers = new HashSet<>(answerRepository.findAllById(request.getAnswerIds()));

        if (answers.isEmpty()) {
            throw new AppException(ErrorCode.ANSWER_NOT_FOUND);
        }

        question.getAnswers().removeAll(answers);
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }
}
