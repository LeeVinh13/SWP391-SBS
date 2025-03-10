package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.QuestionCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuestionResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Question;

@Mapper(componentModel = "spring")
public interface QuestionMapper {
    Question toQuestion(QuestionCreationRequest request);
    QuestionResponse toQuestionResponse(Question question);
}
