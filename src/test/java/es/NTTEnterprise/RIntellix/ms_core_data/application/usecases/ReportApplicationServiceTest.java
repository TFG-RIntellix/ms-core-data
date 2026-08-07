package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.CreateReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

/**
 * Unit tests for {@link ReportApplicationService}.
 * Covers creation, listing, and retrieving reports.
 */
@DisplayName("ReportApplicationService Tests")
@ExtendWith(MockitoExtension.class)
class ReportApplicationServiceTest {

    @Mock
    private ReportPortRepository reportPortRepository;
    
    @Mock
    private RequestPortRepository requestPortRepository;
    
    @Mock
    private CreateReportDTOMapper createReportDTOMapper;
    
    @Mock
    private ReportDTOMapper reportDTOMapper;

    private ReportApplicationService service;

    @BeforeEach
    void setUp() {
        service = new ReportApplicationService(reportPortRepository, requestPortRepository, createReportDTOMapper, reportDTOMapper);
    }

    @Test
    @DisplayName("Should create report and return ID")
    void createReport_success() {
        CreateReportDTO dto = new CreateReportDTO();
        Report report = new Report();
        Report savedReport = new Report();
        savedReport.setId("REP-123");

        when(createReportDTOMapper.toDomain(dto)).thenReturn(report);
        when(reportPortRepository.save(report)).thenReturn(savedReport);

        String id = service.createReport(dto);

        assertEquals("REP-123", id);
        verify(createReportDTOMapper).toDomain(dto);
        verify(reportPortRepository).save(report);
    }

    @Test
    @DisplayName("Should list reports without search param")
    void testListReports_WithoutSearchParam() {
        Report report = new Report();
        ReportDTO reportDTO = new ReportDTO();
        PagedResult<Report> pagedResult = new PagedResult<>(List.of(report), 1, 1, 0, 10);

        when(reportPortRepository.findWithFilters(null, null, 0, 10, "generationDate", "desc"))
            .thenReturn(pagedResult);
        when(reportDTOMapper.toDTO(report)).thenReturn(reportDTO);

        PageResponseDTO<ReportDTO> results = service.listReports(null, 0, 10, "generationDate", "desc");

        assertEquals(1, results.getContent().size());
        assertEquals(reportDTO, results.getContent().get(0));
        verify(requestPortRepository, never()).findRequestIdsBySearch(any());
    }

    @Test
    @DisplayName("Should list reports with search param")
    void testListReports_WithSearchParam() {
        Report report = new Report();
        ReportDTO reportDTO = new ReportDTO();
        PagedResult<Report> pagedResult = new PagedResult<>(List.of(report), 1, 1, 0, 10);
        List<String> requestIds = List.of("REQ-1", "REQ-2");

        when(requestPortRepository.findRequestIdsBySearch("term")).thenReturn(requestIds);
        when(reportPortRepository.findWithFilters("term", requestIds, 0, 10, "generationDate", "desc"))
            .thenReturn(pagedResult);
        when(reportDTOMapper.toDTO(report)).thenReturn(reportDTO);

        PageResponseDTO<ReportDTO> results = service.listReports("term", 0, 10, "generationDate", "desc");

        assertEquals(1, results.getContent().size());
        assertEquals(reportDTO, results.getContent().get(0));
        verify(requestPortRepository).findRequestIdsBySearch("term");
    }

    @Test
    @DisplayName("Should get report by request ID")
    void getReportByRequestId_success() throws EntityNotFoundException {
        Report report = new Report();
        ReportDTO reportDTO = new ReportDTO();

        when(reportPortRepository.findByRequestId("REQ-1")).thenReturn(report);
        when(reportDTOMapper.toDTO(report)).thenReturn(reportDTO);

        ReportDTO result = service.getReportByRequestId("REQ-1");

        assertEquals(reportDTO, result);
    }

    @Test
    @DisplayName("Should get report by ID")
    void getReport_success() throws EntityNotFoundException {
        Report report = new Report();

        when(reportPortRepository.findById("REP-1")).thenReturn(report);

        Report result = service.getReport("REP-1");

        assertEquals(report, result);
    }

    @Test
    @DisplayName("Should throw IllegalArgumentException when getting report with null ID")
    void getReport_nullId() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.getReport(null));
        assertEquals(LogMessage.EXCEPTION_INVALID_REPORT_ID, ex.getMessage());
    }
}
