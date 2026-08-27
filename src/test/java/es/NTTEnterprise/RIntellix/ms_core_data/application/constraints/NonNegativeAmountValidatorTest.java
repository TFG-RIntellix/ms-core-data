package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class NonNegativeAmountValidatorTest {

    private final NonNegativeAmountValidator validator = new NonNegativeAmountValidator();

    @Mock
    private ConstraintValidatorContext context;

    @Test
    @DisplayName("null should be valid")
    void isValid_null() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("zero should be valid")
    void isValid_zero() {
        assertTrue(validator.isValid(0, context));
        assertTrue(validator.isValid(0.0, context));
    }

    @Test
    @DisplayName("positive numbers should be valid")
    void isValid_positive() {
        assertTrue(validator.isValid(10, context));
        assertTrue(validator.isValid(50.5, context));
    }

    @Test
    @DisplayName("negative numbers should be invalid")
    void isValid_negative() {
        assertFalse(validator.isValid(-1, context));
        assertFalse(validator.isValid(-0.1, context));
    }
}
