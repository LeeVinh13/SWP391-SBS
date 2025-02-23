package vn.vinhdeptrai.skincarebookingsystem.exception;

import jakarta.validation.ConstraintViolation;
import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import vn.vinhdeptrai.skincarebookingsystem.dto.response.ApiResponse;

import java.rmi.AccessException;
import java.util.Map;
import java.util.Objects;

import lombok.extern.slf4j.Slf4j;
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String MIN_ATTRIBUTE = "min";
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> handlingRuntimeException(Exception exception) {
        ApiResponse apiRespone = ApiResponse.builder()
                .code(ErrorCode.UNCATEGORIZED_EXCEPTION.getCode())
                .message(ErrorCode.UNCATEGORIZED_EXCEPTION.getMessage())
                .build();
        return ResponseEntity.badRequest().body(apiRespone);
    }
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> handleException(AppException appException) {
        ErrorCode errorCode = appException.getErrorCode();
        ApiResponse apiRespone = ApiResponse.builder()
                .code(errorCode.getCode())
                .message(errorCode.getMessage())
                .build();
        return ResponseEntity.status(errorCode.statusCode).body(apiRespone);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ApiResponse> handlingException(MethodArgumentNotValidException exception) {
        Map<String, Object> attributes = null;
        String emumKey = exception.getFieldError().getDefaultMessage();
        ErrorCode errorCode = ErrorCode.valueOf(emumKey);
        ConstraintViolation<?> constraintViolations = exception.getBindingResult().getAllErrors().getFirst().unwrap(ConstraintViolation.class);
        attributes = constraintViolations.getConstraintDescriptor().getAttributes();
        ApiResponse apiRespone =ApiResponse.builder()
                .code(errorCode.getCode())
                .message(Objects.nonNull(attributes) ? mapAttribute(errorCode.getMessage(), attributes) : errorCode.getMessage())
                .build();
         return ResponseEntity.badRequest().body(apiRespone);
    }
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseEntity<ApiResponse> handlingAccessDeniedException(AccessDeniedException exception) {
        ErrorCode errorCode = ErrorCode.UNAUTHORIZED;
        return ResponseEntity.status(errorCode.statusCode)
                .body(ApiResponse.builder()
                        .code(errorCode.getCode())
                        .message(errorCode.getMessage())
                        .build());
    }
    private String mapAttribute(String message, Map<String, Object> attributes) {
        String minValue = String.valueOf(attributes.get(MIN_ATTRIBUTE));
        return message.replace("{" + MIN_ATTRIBUTE + "}", minValue);
    }

}
