package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;

class CreateReportDTOMapperTest {

    private final CreateReportDTOMapper mapper = new CreateReportDTOMapper();

    @Test
    @DisplayName("Should map null DTO to null")
    void toDomain_null() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map CreateReportDTO to Report domain entity successfully")
    void toDomain_success() {
        CreateReportDTO dto = new CreateReportDTO();
        dto.setPartyId("P-1");
        dto.setRequestId("REQ-1");
        dto.setScoringId("SCO-1");
        dto.setReportType("RISK_ANALYSIS");
        dto.setTitle("Test Report");
        dto.setAiSummary("AI Summary");
        dto.setRiskAnalysis("Risk Analysis");
        dto.setRecommendations(List.of("Rec 1"));
        dto.setFilePath("/path/to/report.pdf");
        dto.setFileSizeBytes(1024);
        dto.setGeneratedBy("AI");
        Date now = new Date();
        dto.setGeneratedDate(now);
        dto.setGenerationTimeMs(100);
        dto.setModelVersion("v1.0");
        dto.setLanguage("es");

        CreateReportDTO.RiskFactorDTO factorDTO = new CreateReportDTO.RiskFactorDTO();
        factorDTO.setFactor("Income");
        factorDTO.setSeverity("ALTO");
        factorDTO.setDescription("Low income");
        dto.setRiskFactors(List.of(factorDTO));

        Report report = mapper.toDomain(dto);

        assertEquals("P-1", report.getPartyId());
        assertEquals("REQ-1", report.getRequestId());
        assertEquals("SCO-1", report.getScoringId());
        assertEquals(ReportType.RISK_ANALYSIS, report.getReportType());
        assertEquals("Test Report", report.getTitle());
        assertEquals("AI Summary", report.getAiSummary());
        assertEquals("Risk Analysis", report.getRiskAnalysis());
        assertEquals(List.of("Rec 1"), report.getRecommendations());
        assertEquals("/path/to/report.pdf", report.getFilePath());
        assertEquals(1024, report.getFileSizeBytes());
        assertEquals("AI", report.getGeneratedBy());
        assertEquals(now, report.getGeneratedDate());
        assertEquals(100, report.getGenerationTimeMs());
        assertEquals("v1.0", report.getModelVersion());
        assertEquals("es", report.getLanguage());

        assertEquals(1, report.getRiskFactors().size());
        assertEquals("Income", report.getRiskFactors().get(0).getFactor());
        assertEquals(Severity.ALTO, report.getRiskFactors().get(0).getSeverity());
        assertEquals("Low income", report.getRiskFactors().get(0).getDescription());
    }

    @Test
    @DisplayName("Should throw exception for invalid report type")
    void toDomain_invalidReportType() {
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportType("INVALID_TYPE");

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(dto));
    }

    @Test
    @DisplayName("Should throw exception for invalid severity in risk factors")
    void toDomain_invalidSeverity() {
        CreateReportDTO dto = new CreateReportDTO();
        dto.setReportType("RISK_ANALYSIS");
        CreateReportDTO.RiskFactorDTO factor = new CreateReportDTO.RiskFactorDTO();
        factor.setSeverity("INVALID_SEVERITY");
        dto.setRiskFactors(List.of(factor));

        assertThrows(IllegalArgumentException.class, () -> mapper.toDomain(dto));
    }
}
