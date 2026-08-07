package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class ValidRiskGradeValidatorTest {

    private final ValidRiskGradeValidator validator = new ValidRiskGradeValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    @DisplayName("null or blank should be valid")
    void isValid_nullOrBlank() {
        assertTrue(validator.isValid(null, context));
        assertTrue(validator.isValid("", context));
        assertTrue(validator.isValid("   ", context));
    }

    @Test
    @DisplayName("valid enum values should be valid")
    void isValid_validRiskGrade() {
        assertTrue(validator.isValid("A", context));
        assertTrue(validator.isValid("B", context));
        assertTrue(validator.isValid("C", context));
        assertTrue(validator.isValid("a", context)); // should handle case insensitivity based on trim/upper
        assertTrue(validator.isValid(" A ", context)); // should handle spaces
    }

    @Test
    @DisplayName("invalid enum values should be invalid")
    void isValid_invalidRiskGrade() {
        assertFalse(validator.isValid("INVALID_GRADE", context));
        assertFalse(validator.isValid("UNKNOWN", context));
    }
}
