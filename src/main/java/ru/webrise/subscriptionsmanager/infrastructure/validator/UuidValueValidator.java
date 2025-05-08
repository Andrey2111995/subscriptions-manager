package ru.webrise.subscriptionsmanager.infrastructure.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.apache.logging.log4j.util.Strings;

import java.util.UUID;

public class UuidValueValidator implements ConstraintValidator<UuidNotBlank, String> {

    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (Strings.isBlank(string)) {
            return false;
        } else {
            try {
                UUID.fromString(string);
                return true;
            } catch (Exception e) {
                return false;
            }
        }
    }
}
