package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @NonNegativeAmount constraint.
 * Checks that amount (if provided) is greater than or equal to 0.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class NonNegativeAmountValidator implements ConstraintValidator<NonNegativeAmount, Number> {

    @Override
    public boolean isValid(Number value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value.doubleValue() >= 0;
    }
}
