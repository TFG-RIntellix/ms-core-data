package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RiskGrade;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

/**
 * Validator implementation for @ValidRiskGrade constraint.
 * Checks that the risk grade string is a valid enum value.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
public class ValidRiskGradeValidator implements ConstraintValidator<ValidRiskGrade, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank()) {
            return true;
        }

        try {
            RiskGrade.valueOf(value.trim().toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
