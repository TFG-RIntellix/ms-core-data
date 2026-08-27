package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.ReportMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.ReportRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

@DisplayName("ReportRepositoryAdapter Tests")
@ExtendWith(MockitoExtension.class)
class ReportRepositoryAdapterTest {

    @Mock
    private ReportRepository reportRepository;

    @Mock
    private ReportMapper reportMapper;

    private ReportRepositoryAdapter adapter;

    @BeforeEach
    void setUp() {
        adapter = new ReportRepositoryAdapter(reportRepository, reportMapper);
    }

    @Test
    @DisplayName("findByRequestId should throw IllegalArgumentException when requestId is null or empty")
    void testFindByRequestId_EmptyOrNull() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class,
                () -> adapter.findByRequestId(null));
        assertEquals(LogMessage.EXCEPTION_INVALID_REQUEST_ID, ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class,
                () -> adapter.findByRequestId("   "));
        assertEquals(LogMessage.EXCEPTION_INVALID_REQUEST_ID, ex2.getMessage());
    }

    @Test
    @DisplayName("findByRequestId should throw EntityNotFoundException with format when ObjectId is invalid")
    void testFindByRequestId_InvalidObjectId() {
        String invalidId = "not-an-object-id";
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adapter.findByRequestId(invalidId));
        assertEquals(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_REQUEST, invalidId), ex.getMessage());
    }

    @Test
    @DisplayName("findById should throw EntityNotFoundException with correct message when not found")
    void testFindById_NotFound() {
        String validId = new ObjectId().toHexString();
        when(reportRepository.findById(any(ObjectId.class))).thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class, () -> adapter.findById(validId));
        assertEquals(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_ID, validId), ex.getMessage());
    }

    @Test
    @DisplayName("findByRequestId should throw EntityNotFoundException with format when entity is not found")
    void testFindByRequestId_NotFound() {
        String validId = new ObjectId().toHexString();
        when(reportRepository.findFirstByRequestIdOrderByGeneratedDateDesc(any(ObjectId.class)))
                .thenReturn(Optional.empty());

        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> adapter.findByRequestId(validId));
        assertEquals(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_REQUEST, validId), ex.getMessage());
    }
}
