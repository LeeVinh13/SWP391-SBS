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

    USER_NOT_FOUND(1005,"USER NOT FOUND!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1006,"ROLE NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_NOT_FOUND(1009,"SERVICE NOT FOUND!", HttpStatus.NOT_FOUND),
    CATE_SERVICE_NOT_FOUND(1010,"CATEGORY_SERVICE NOT FOUND!", HttpStatus.NOT_FOUND),


    UNAUTHENTICATED(1007,"UNAUTHENTICATED!", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1008, "You do not have permission", HttpStatus.FORBIDDEN),
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
