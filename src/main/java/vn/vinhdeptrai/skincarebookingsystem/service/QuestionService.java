package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Transactional
    public QuestionResponse createWithAnswers(QuestionCreationWithAnswersRequest request, int quizID) {
        Quiz quiz = quizRepository.findById(quizID)
                .orElseThrow(() -> new AppException(ErrorCode.QUIZ_NOT_FOUND));

        if (questionRepository.existsByQuestion(request.getQuestion())) {
            throw new AppException(ErrorCode.QUESTION_EXISTED);
        }

        Question question = Question.builder()
                .quizzes(new HashSet<>())
                .question(request.getQuestion())
                .answers(new HashSet<>())
                .build();

        question.getQuizzes().add(quiz);
        quiz.getQuestions().add(question);

        if (request.getAnswerRequestList() != null) {
            for (AnswerRequest answerRequest : request.getAnswerRequestList()) {
                Answer answer = answerMapper.toAnswer(answerRequest);
                answer.setQuestion(question);
                question.getAnswers().add(answer);
            }
        }

        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @Transactional
    public QuestionResponse update(int questionId, QuestionUpdateRequest questionUpdateRequest) {

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        question.setQuestion(questionUpdateRequest.getQuestion());

        if (questionUpdateRequest.getAnswers() != null) {
            if (question.getAnswers() != null) {
                answerRepository.deleteAll(question.getAnswers());
                answerRepository.flush();
                question.getAnswers().clear();
                Set<Answer> updatedAnswers = questionUpdateRequest.getAnswers().stream()
                        .map(answerMapper::toAnswer)
                        .collect(Collectors.toSet());
                updatedAnswers.forEach(answer -> answer.setQuestion(question));
                question.getAnswers().addAll(updatedAnswers);
            }
        }
        return questionMapper.toQuestionResponse(questionRepository.save(question));
    }

    @Transactional
    public void delete(int questionId) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        for (Quiz quiz : question.getQuizzes()) {
            quiz.getQuestions().remove(question);
        }
        quizRepository.saveAll(question.getQuizzes());

        if (!question.getAnswers().isEmpty()) {
            answerRepository.deleteAll(question.getAnswers());
        }
        questionRepository.delete(question);
    }

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
