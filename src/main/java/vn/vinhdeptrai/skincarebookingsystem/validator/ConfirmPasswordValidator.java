package vn.vinhdeptrai.skincarebookingsystem.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import vn.vinhdeptrai.skincarebookingsystem.dto.request.RegisterRequest;

public class ConfirmPasswordValidator implements ConstraintValidator<ConfirmPasswordConstraint, RegisterRequest> {


    @Override
    public void initialize(ConfirmPasswordConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(RegisterRequest registerRequest, ConstraintValidatorContext context) {
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("INVALID_CONFIRM_PASSWORD")
                    .addPropertyNode("confirmPassword")
                    .addConstraintViolation();
            return false;
        }
        return true;
    }
}
