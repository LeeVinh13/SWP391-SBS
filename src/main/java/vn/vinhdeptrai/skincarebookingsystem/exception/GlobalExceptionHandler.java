package vn.vinhdeptrai.skincarebookingsystem.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleException(AppException appException) {
        ErrorCode errorCode = appException.getErrorCode();
        ApiResponse apiRespone = new ApiResponse();
        apiRespone.setCode(errorCode.getCode());
        apiRespone.setMessage(errorCode.getMessage());
        return ResponseEntity.status(errorCode.statusCode).body(apiRespone);
    }

}
