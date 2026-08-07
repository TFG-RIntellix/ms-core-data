package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

/**
 * Validation constraint to ensure a risk grade is one of the valid enum values.
 * Can be applied to String fields representing risk grades.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
@Target({ ElementType.FIELD, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidRiskGradeValidator.class)
public @interface ValidRiskGrade {

    String message() default "Risk grade must be one of the valid values (A, B, C, D, E, F, G, H)";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
