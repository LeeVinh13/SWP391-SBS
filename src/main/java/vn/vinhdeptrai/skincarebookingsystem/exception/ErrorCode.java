package vn.vinhdeptrai.skincarebookingsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"UNCATEGORIZED EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1001,"USER ALREADY EXISTED!", HttpStatus.CONFLICT),

    INVALID_USERNAME(1002, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1003, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(1004, "Confirm Password and Password does not match", HttpStatus.BAD_REQUEST),

    UNAUTHENTICATED(1005,"UNAUTHENTICATED!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),

    SLOT_NOT_FOUND(1012, "Slot not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1007,"USER NOT FOUND!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1008,"ROLE NOT FOUND!", HttpStatus.NOT_FOUND),
    THERAPIST_NOT_FOUND(1011,"THERAPIS NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_NOT_FOUND(1009,"SERVICE NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_CATE_NOT_FOUND(1010,"SERVICE CATEGORY NOT FOUND!", HttpStatus.NOT_FOUND),

    QUIZ_NOT_FOUND(2000,"QUIZ NOT FOUND!", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(2001,"QUESTION NOT FOUND!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXIST_QUIZ(2002,"THIS CATEGORY ALREADY HAVE A QUIZ", HttpStatus.BAD_REQUEST),
    QUESTION_EXISTED(2003,"QUESTION EXISTED!", HttpStatus.CONFLICT),
    ANSWER_NOT_FOUND(2004,"ANSWER NOT FOUND!", HttpStatus.NOT_FOUND),
    ;
    public int code;
    public String message;
    public HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
