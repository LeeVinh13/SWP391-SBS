package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AnswerRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.AnswerResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Answer;

@Mapper(componentModel = "spring")
public interface AnswerMapper {
    Answer toAnswer(AnswerRequest answerRequest);
    AnswerResponse toAnswerResponse(Answer answer);
}
