package vn.vinhdeptrai.skincarebookingsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999,"UNCATEGORIZED EXCEPTION", HttpStatus.INTERNAL_SERVER_ERROR),
    USER_EXISTED(1007,"USER ALREADY EXISTED!", HttpStatus.CONFLICT),

    INVALID_USERNAME(1001, "Username must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_PASSWORD(1002, "Password must be at least {min} characters", HttpStatus.BAD_REQUEST),
    INVALID_CONFIRM_PASSWORD(1008, "Confirm Password and Password does not match", HttpStatus.BAD_REQUEST),

    USER_NOT_FOUND(1003,"USER NOT FOUND!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1004,"ROLE NOT FOUND!", HttpStatus.NOT_FOUND),

    UNAUTHENTICATED(1005,"UNAUTHENTICATED!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "You do not have permission", HttpStatus.FORBIDDEN),
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
