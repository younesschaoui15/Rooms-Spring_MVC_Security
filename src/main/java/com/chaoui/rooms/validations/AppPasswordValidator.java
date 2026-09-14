package com.chaoui.rooms.validations;

import com.chaoui.rooms.annotations.AppPassword;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Valid;

public class AppPasswordValidator implements ConstraintValidator<AppPassword, String> {

    AppPassword appPassword;

    @Override
    public void initialize(@Valid AppPassword constraintAnnotation) {
        appPassword = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (appPassword.min() > value.length())
            return false;

        if (appPassword.max() < value.length())
            return false;

        if (appPassword.requireUppercase() && !value.equals(value.toUpperCase()))
            return false;

        if (appPassword.requireSpecialCharacters()) {
            boolean containsSpecialCharacters = value.chars()
                .anyMatch(c -> appPassword.specialCharacters().indexOf(c) >= 0);

            if (!containsSpecialCharacters)
                return false;
        }

        return true;
    }
}