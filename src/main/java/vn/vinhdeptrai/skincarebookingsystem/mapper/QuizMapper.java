package vn.vinhdeptrai.skincarebookingsystem.mapper;

import org.mapstruct.Mapper;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuizResponse;
import vn.vinhdeptrai.skincarebookingsystem.entity.Quiz;

@Mapper(componentModel = "spring")
public interface QuizMapper {
    QuizResponse toQuizResponse(Quiz quiz);
}
