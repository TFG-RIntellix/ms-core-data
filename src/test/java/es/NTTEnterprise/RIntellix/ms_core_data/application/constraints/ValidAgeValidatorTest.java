package es.NTTEnterprise.RIntellix.ms_core_data.application.constraints;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ConstraintValidatorContext;

@ExtendWith(MockitoExtension.class)
class ValidAgeValidatorTest {

    private final ValidAgeValidator validator = new ValidAgeValidator();

    @Mock
    private ValidAge annotation;

    @Mock
    private ConstraintValidatorContext context;

    @BeforeEach
    void setUp() {
        when(annotation.minAge()).thenReturn(18);
        validator.initialize(annotation);
    }

    @Test
    @DisplayName("null should be valid")
    void isValid_null() {
        assertTrue(validator.isValid(null, context));
    }

    @Test
    @DisplayName("age greater or equal to minAge should be valid")
    void isValid_validAge() {
        assertTrue(validator.isValid(18, context));
        assertTrue(validator.isValid(30, context));
    }

    @Test
    @DisplayName("age less than minAge should be invalid")
    void isValid_invalidAge() {
        assertFalse(validator.isValid(17, context));
        assertFalse(validator.isValid(0, context));
    }
}
