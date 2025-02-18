package vn.vinhdeptrai.skincarebookingsystem.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
@Getter
public enum ErrorCode {
    USER_EXISTED(1000,"USER ALREADY EXISTED!", HttpStatus.CONFLICT),
    USER_NOT_FOUND(1001,"USER NOT FOUND!", HttpStatus.NOT_FOUND),;
    public int code;
    public String message;
    public HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
