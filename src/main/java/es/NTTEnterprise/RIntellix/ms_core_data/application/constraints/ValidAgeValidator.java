package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @ValidAge constraint.
 * Checks that age (if provided) is at least the minimum age value.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
public class ValidAgeValidator implements ConstraintValidator<ValidAge, Integer> {

    private int minAge;

    @Override
    public void initialize(ValidAge annotation) {
        this.minAge = annotation.minAge();
    }

    @Override
    public boolean isValid(Integer value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return value >= minAge;
    }
}
