package vn.vinhdeptrai.skincarebookingsystem.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.AddQuestionsToQuizRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.QuizCreationRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.QuizUpdateRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RemoveQuestionsFromQuizRequest;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.QuizResponse;
import vn.vinhdeptrai.skincarebookingsystem.service.QuizService;

import java.util.List;


@Slf4j
@RestController
@RequestMapping("/api/quizzes")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class QuizController {
    QuizService quizService;

    @GetMapping
    public ApiResponse<List<QuizResponse>> getAll() {
        return ApiResponse.<List<QuizResponse>>builder()
                .result(quizService.getQuizList())
                .build();
    }

    @GetMapping("/{quizId}")
    public ApiResponse<QuizResponse> getQuizById(@PathVariable int quizId) {
        return ApiResponse.<QuizResponse>builder()
                .result(quizService.getQuiz(quizId))
                .build();
    }

    @PostMapping("/create")
    public ApiResponse<QuizResponse> create(@RequestBody QuizCreationRequest request) {
        ApiResponse<QuizResponse> apiResponse = new ApiResponse<>();
        apiResponse.setResult(quizService.create(request));
        return apiResponse;
    }

    @PutMapping("/update/{quizId}")
    ApiResponse<QuizResponse> update(@PathVariable int quizId, @RequestBody QuizUpdateRequest quizUpdateRequest) {
        return ApiResponse.<QuizResponse>builder()
                .result(quizService.update(quizId, quizUpdateRequest))
                .build();
    }

    @DeleteMapping("/delete/{quizId}")
    void deleteQuiz(@PathVariable int quizId) {
        this.quizService.delete(quizId);
    }

    @PostMapping("/add-questions-to/{quizId}")
    public ApiResponse<QuizResponse> addQuestionsToQuiz
            (@PathVariable int quizId, @RequestBody AddQuestionsToQuizRequest request) {
        return ApiResponse.<QuizResponse>builder()
                .result(quizService.addQuestionsToQuiz(quizId, request))
                .build();
    }

    @DeleteMapping("/remove-questions-from/{quizId}")
    public ApiResponse<QuizResponse> removeQuestionsFromQuiz
            (@PathVariable int quizId, @RequestBody RemoveQuestionsFromQuizRequest request) {
        return ApiResponse.<QuizResponse>builder()
                .result(quizService.removeQuestionsFromQuiz(quizId, request))
                .build();
    }
}
