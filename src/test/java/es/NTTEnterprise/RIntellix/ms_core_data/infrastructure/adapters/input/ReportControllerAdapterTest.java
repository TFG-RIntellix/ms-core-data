package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input.exceptions.GlobalExceptionHandler;

@DisplayName("ReportControllerAdapter Tests")
@ExtendWith(MockitoExtension.class)
class ReportControllerAdapterTest {

    private MockMvc mockMvc;

    @Mock
    private ReportPortService reportPortService;

    @InjectMocks
    private ReportControllerAdapter controller;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    @DisplayName("GET /api/reports should return 200 with list of reports")
    void testListReports() throws Exception {
        PageResponseDTO<ReportDTO> response = new PageResponseDTO<>(List.of(new ReportDTO()), 1, 1, 0, 10);
        
        when(reportPortService.listReports(eq("term"), eq(0), eq(10), eq("generationDate"), eq("desc")))
            .thenReturn(response);

        mockMvc.perform(get("/api/reports")
                .param("search", "term")
                .param("page", "0")
                .param("size", "10")
                .param("sortBy", "generationDate")
                .param("sortDir", "desc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/reports?requestId={id} should return 200 with report")
    void testGetReportByRequestId_Found() throws Exception {
        ReportDTO report = new ReportDTO();
        report.setReportId("rep-1");
        
        when(reportPortService.getReportByRequestId("req-1")).thenReturn(report);

        mockMvc.perform(get("/api/reports")
                .param("requestId", "req-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.reportId").value("rep-1"));
    }

    @Test
    @DisplayName("GET /api/reports?requestId={id} should return 404 with JSON error when not found")
    void testGetReportByRequestId_NotFound() throws Exception {
        when(reportPortService.getReportByRequestId("req-99"))
            .thenThrow(new es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException("No report found"));

        mockMvc.perform(get("/api/reports")
                .param("requestId", "req-99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("No report found"));
    }
}
