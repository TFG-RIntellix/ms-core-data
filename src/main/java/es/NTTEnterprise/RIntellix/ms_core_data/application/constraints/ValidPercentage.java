package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validation constraint to ensure a percentage value is between 0 and 1.
 * Can be applied to Double/Float fields representing percentages or ratios.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidPercentageValidator.class)
public @interface ValidPercentage {

    String message() default "Value must be between 0 and 1";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
