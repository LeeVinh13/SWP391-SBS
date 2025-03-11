package vn.vinhdeptrai.skincarebookingsystem.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AnswerRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AnswerResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;
import vn.vinhdeptrai.skincarebookingsystem.exception.AppException;
import vn.vinhdeptrai.skincarebookingsystem.exception.ErrorCode;
import vn.vinhdeptrai.skincarebookingsystem.mapper.AnswerMapper;
import vn.vinhdeptrai.skincarebookingsystem.repository.AnswerRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AnswerService {
    private final AnswerRepository answerRepository;
    AnswerMapper answerMapper;

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

    @PreAuthorize("hasRole('ADMIN')")
    public AnswerResponse update(int answerId, AnswerRequest request) {
        //chỉ update nội dung câu trả lời và điểm vì ở question sẽ có API add hoặc remove answer

        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));

        answer = Answer.builder()
                .answer(request.getAnswer())
                .score(request.getScore())
                .build();
        return answerMapper.toAnswerResponse(answerRepository.save(answer));
    }

    @PreAuthorize("hasRole('ADMIN')")
    public void delete(int answerId) {
        Answer answer = answerRepository.findById(answerId)
                .orElseThrow(() -> new AppException(ErrorCode.ANSWER_NOT_FOUND));
        answerRepository.delete(answer);
    }
}
