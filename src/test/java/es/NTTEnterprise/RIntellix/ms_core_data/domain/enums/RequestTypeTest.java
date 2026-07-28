package es.NTTEnterprise.RIntellix.ms_core_data.domain.enums;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class RequestTypeTest {

    @Test
    @DisplayName("Should return correct string value")
    void getValue_success() {
        assertEquals("PRESTAMO", RequestType.PRESTAMO.getValue());
        assertEquals("HIPOTECA", RequestType.HIPOTECA.getValue());
        assertEquals("TARJETA DE CREDITO", RequestType.TARJETA_CREDITO.getValue());
    }

    @Test
    @DisplayName("Should parse from string value correctly")
    void fromValue_success() {
        assertEquals(RequestType.PRESTAMO, RequestType.fromValue("PRESTAMO"));
        assertEquals(RequestType.HIPOTECA, RequestType.fromValue("HIPOTECA"));
        // Matches exact value
        assertEquals(RequestType.TARJETA_CREDITO, RequestType.fromValue("TARJETA DE CREDITO"));
        // Matches enum name
        assertEquals(RequestType.TARJETA_CREDITO, RequestType.fromValue("TARJETA_CREDITO"));
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when parsing null")
    void fromValue_null() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RequestType.fromValue(null));
        assertNotNull(exception.getMessage());
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when parsing unknown value")
    void fromValue_unknown() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> RequestType.fromValue("UNKNOWN_TYPE"));
        assertTrue(exception.getMessage().contains("UNKNOWN_TYPE"));
    }
}
