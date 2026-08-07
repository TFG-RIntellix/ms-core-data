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
class ValidPercentageValidatorTest {

    private final ValidPercentageValidator validator = new ValidPercentageValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    @DisplayName("null should be valid")
    void isValid_null() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("values between 0.0 and 1.0 should be valid")
    void isValid_validPercentage() {
        assertTrue(validator.isValid(0.0, context));
        assertTrue(validator.isValid(0.5, context));
        assertTrue(validator.isValid(1.0, context));
    }

    @Test
    @DisplayName("values outside 0.0 and 1.0 should be invalid")
    void isValid_invalidPercentage() {
        assertFalse(validator.isValid(-0.1, context));
        assertFalse(validator.isValid(1.1, context));
    }
}
