package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validation constraint to ensure age is at least 18.
 * Can be applied to Integer fields representing age.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidAgeValidator.class)
public @interface ValidAge {

    String message() default "Age must be at least 18";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    int minAge() default 18;
}
