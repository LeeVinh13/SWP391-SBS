package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AnswerRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AnswerResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.AnswerMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AnswerRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuestionRepository;
import vn.vinhdeptrai.skincarebookingsystem.repository.QuizRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerService {
    private final AnswerRepository answerRepository;
    AnswerMapper answerMapper;
    private final QuestionRepository questionRepository;
    private final QuizRepository quizRepository;

    public List<AnswerResponse> getAnswerList() {
        List<Answer> answerList = answerRepository.findAll();
        return answerList.stream().map(answerMapper::toAnswerResponse).collect(Collectors.toList());
    }

    public AnswerResponse getAnswer(int id){
        Answer answer = answerRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
        return answerMapper.toAnswerResponse(answer);
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AnswerResponse create(AnswerRequest request) {
        //không check existed answer vì có thể có nhiều câu hỏi cần trả lời giống nhau cùng trọng số
        //Ex: câu trả lời về thời gian hoặc mức độ

        Answer answer = answerMapper.toAnswer(request);

        return answerMapper.toAnswerResponse(answerRepository.save(answer));
    }

    public AnswerResponse createForQuestion(AnswerRequest request, int questionID) {
        Question question = questionRepository.findById(questionID)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));

        Answer answer = answerMapper.toAnswer(request);
        answer.setQuestion(question);
        return answerMapper.toAnswerResponse(answerRepository.save(answer));
    }

    public List<AnswerResponse> createListAnswers(List<AnswerRequest> request, int questionID) {
        Question question = questionRepository.findById(questionID)
                .orElseThrow(() -> new AppException(ErrorCode.QUESTION_NOT_FOUND));
        List<AnswerResponse> answerList = new ArrayList<>();

        for (AnswerRequest answerRequest : request) {
            Answer answer = new Answer();
            answer = answerMapper.toAnswer(answerRequest);
            answer.setQuestion(question);
            answerList.add(answerMapper.toAnswerResponse(answer));
        }

        return answerList;
    }

    @PreAuthorize("hasRole('ADMIN')")
    public AnswerResponse update(int answerId, AnswerRequest request) {
        //chỉ update nội dung câu trả lời và điểm vì ở question sẽ có API add hoặc remove answer

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));

        answer.setAnswer(request.getAnswer());
        answer.setScore(request.getScore());
        return answerMapper.toAnswerResponse(answerRepository.save(answer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
        answerRepository.delete(answer);
    }
}
