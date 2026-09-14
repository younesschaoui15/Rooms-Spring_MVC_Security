package com.chaoui.rooms.annotations;

import com.chaoui.rooms.validations.AppPasswordValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

@Target({FIELD, METHOD, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = AppPasswordValidator.class)
public @interface AppPassword {

    String message() default "Password must meet the security requirements";

    int min() default 8;

    int max() default 128;

    @NotBlank String specialCharacters() default "!@#$%^&*()_+-=[]{}|;:,.<>?";

    boolean requireSpecialCharacters() default true;

    boolean requireUppercase() default false;

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
