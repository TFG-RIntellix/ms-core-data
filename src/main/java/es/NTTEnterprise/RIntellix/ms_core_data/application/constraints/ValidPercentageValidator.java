package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @ValidPercentage constraint.
 * Checks that value (if provided) is between 0 and 1 (inclusive).
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
public class ValidPercentageValidator implements ConstraintValidator<ValidPercentage, Double> {

    @Override
    public boolean isValid(Double value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= 0.0 && value <= 1.0;
    }
}
