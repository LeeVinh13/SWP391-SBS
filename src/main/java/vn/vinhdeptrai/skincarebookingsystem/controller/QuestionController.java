package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuestionResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.QuestionService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/questions")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuestionController {
    QuestionService questionService;

    @GetMapping
    public ApiResponse<List<QuestionResponse>> getAll() {
        return ApiResponse.<List<QuestionResponse>>builder()
                .result(questionService.getQuestionList())
                .build();
    }

    @GetMapping("/{questionId}")
    public ApiResponse<QuestionResponse> getQuestionById(@PathVariable int questionId) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.getQuestion(questionId))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<QuestionResponse> create(@RequestBody QuestionCreationRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.create(request))
                .build();
    }

    @PutMapping("/update/{questionId}")
    ApiResponse<QuestionResponse> update(@PathVariable int questionId, @RequestBody QuestionUpdateRequest questionUpdateRequest) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.update(questionId, questionUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{questionId}")
    void deleteQuestion(@PathVariable int questionId) {
        this.questionService.delete(questionId);
    }

    @PostMapping("/add-answers-to/{questionId}")
    public ApiResponse<QuestionResponse> addAnswersToQuestion
            (@PathVariable int questionId, @RequestBody AddAnswersToQuestionRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.addAnswersToQuestion(questionId, request))
                .build();
    }

    @DeleteMapping("/remove-answers-from/{questionId}")
    public ApiResponse<QuestionResponse> removeAnswersFromQuestion
            (@PathVariable int questionId, @RequestBody RemoveAnswersFromQuestionRequest request) {
        return ApiResponse.<QuestionResponse>builder()
                .result(questionService.removeAnswersFromQuestion(questionId, request))
                .build();
    }
}
