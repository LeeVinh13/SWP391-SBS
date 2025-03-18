package vn.vinhdeptrai.skincarebookingsystem.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
@FieldDefaults(level = AccessLevel.PUBLIC)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Uncategorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001, "User already existed!", HttpStatus.CONFLICT),

    INVALID_USERNAME(1002, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(1004, "Confirm password and password do not match", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1005, "Unauthenticated!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),

    SLOT_NOT_AVAILABLE(1012, "Slot is not available", HttpStatus.CONFLICT),
    APPOINTMENT_CANCELLED(1013, "Appointment cancelled", HttpStatus.CONFLICT),
    SLOT_NOT_FOUND(1013, "Slot not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1007, "User not found!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1008, "Role not found!", HttpStatus.NOT_FOUND),
    THERAPIST_NOT_FOUND(1011, "Therapist not found!", HttpStatus.NOT_FOUND),
    SERVICE_NOT_FOUND(1009, "Service not found!", HttpStatus.NOT_FOUND),
    SERVICE_CATE_NOT_FOUND(1010, "Service category not found!", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_FOUND(1014, "Appointment not found!", HttpStatus.NOT_FOUND),

    CATEGORY_CONTAINING_SERVICE(1015, "Cannot delete category because it contain service!", HttpStatus.NOT_FOUND),

    QUIZ_NOT_FOUND(2000, "Quiz not found!", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(2001, "Question not found!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXIST_QUIZ(2002, "This category already has a quiz", HttpStatus.BAD_REQUEST),
    QUESTION_EXISTED(2003, "Question existed!", HttpStatus.CONFLICT),
    ANSWER_NOT_FOUND(2004, "Answer not found!", HttpStatus.NOT_FOUND),
    SERVICE_RECOMMENDATION_NOT_FOUND(2005, "Service recommendation not found!", HttpStatus.NOT_FOUND),
    SERVICE_RECOMMENDATION_EXISTED(2006, "Service recommendation existed!", HttpStatus.CONFLICT);
    int code;
    String message;
    HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
