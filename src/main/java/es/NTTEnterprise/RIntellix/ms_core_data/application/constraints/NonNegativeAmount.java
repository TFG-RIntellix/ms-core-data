package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validation constraint to ensure a monetary amount is non-negative.
 * Can be applied to numeric fields representing amounts.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = NonNegativeAmountValidator.class)
public @interface NonNegativeAmount {

    String message() default "Amount cannot be negative";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
