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

    SLOT_NOT_AVAILABLE(1012, "Slot is not available", HttpStatus.CONFLICT),
    APPOINTMENT_CANCELLED(1013, "Appointment cancelled", HttpStatus.CONFLICT),
    SLOT_NOT_FOUND(1013, "Slot not found", HttpStatus.NOT_FOUND),
    USER_NOT_FOUND(1007,"USER NOT FOUND!", HttpStatus.NOT_FOUND),
    ROLE_NOT_FOUND(1008,"ROLE NOT FOUND!", HttpStatus.NOT_FOUND),
    THERAPIST_NOT_FOUND(1011,"THERAPIS NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_NOT_FOUND(1009,"SERVICE NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_CATE_NOT_FOUND(1010,"SERVICE CATEGORY NOT FOUND!", HttpStatus.NOT_FOUND),
    APPOINTMENT_NOT_FOUND(1014,"APPOINTMENT NOT FOUND!", HttpStatus.NOT_FOUND),

    QUIZ_NOT_FOUND(2000,"QUIZ NOT FOUND!", HttpStatus.NOT_FOUND),
    QUESTION_NOT_FOUND(2001,"QUESTION NOT FOUND!", HttpStatus.NOT_FOUND),
    CATEGORY_ALREADY_EXIST_QUIZ(2002,"THIS CATEGORY ALREADY HAVE A QUIZ", HttpStatus.BAD_REQUEST),
    QUESTION_EXISTED(2003,"QUESTION EXISTED!", HttpStatus.CONFLICT),
    ANSWER_NOT_FOUND(2004,"ANSWER NOT FOUND!", HttpStatus.NOT_FOUND),
    SERVICE_RECOMMENDATION_NOT_FOUND(2005,"SERVICE RECOMMENDATION NOT FOUND!", HttpStatus.NOT_FOUND), 
    SERVICE_RECOMMENDATION_EXISTED(2006,"SERVICE RECOMMENDATION EXISTED!", HttpStatus.CONFLICT),
    BLOG_NOT_FOUND(2007,"BLOG NOT FOUND!", HttpStatus.NOT_FOUND),
    RATING_NOT_FOUND(2008,"RATING NOT FOUND!", HttpStatus.NOT_FOUND),
    RATING_ALREADY_EXIST(2009,"RATING ALREADY EXIST!", HttpStatus.CONFLICT),
    APPOINTMENT_NOT_APPROVED(2010,"APPOINTMENT NOT APPROVED!", HttpStatus.CONFLICT),
    INVALID_OLD_PASSWORD(2011, "Old password is incorrect", HttpStatus.BAD_REQUEST),
    CONFIRM_PASSWORD_NOT_MATCH(2012, "New password and confirm password do not match", HttpStatus.BAD_REQUEST);
    public int code;
    public String message;
    public HttpStatusCode statusCode;

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }
}
