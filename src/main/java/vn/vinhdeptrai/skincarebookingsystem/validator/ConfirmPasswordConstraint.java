package vn.vinhdeptrai.skincarebookingsystem.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;
// cho phép annotation apply lên attribute, class
@Target({ElementType.FIELD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {ConfirmPasswordValidator.class})
public @interface ConfirmPasswordConstraint {
    String message() default "Invalid confirm password";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
