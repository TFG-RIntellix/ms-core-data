package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;

class ReportDTOMapperTest {

    private final ReportDTOMapper mapper = new ReportDTOMapper();

    @Test
    @DisplayName("Should map null domain entity to null")
    void toDTO_null() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("Should map Report domain entity to ReportDTO successfully")
    void toDTO_success() {
        Report report = new Report();
        report.setId("REP-1");
        report.setPartyId("P-1");
        report.setRequestId("REQ-1");
        report.setScoringId("SCO-1");
        report.setReportType(ReportType.RISK_ANALYSIS);
        report.setTitle("Title");
        report.setAiSummary("Summary");
        report.setRiskAnalysis("Analysis");
        report.setRecommendations(List.of("Rec 1"));
        report.setFilePath("/path.pdf");
        report.setFileSizeBytes(500);
        report.setGeneratedBy("User");
        Date now = new Date();
        report.setGeneratedDate(now);
        report.setGenerationTimeMs(150);
        report.setModelVersion("v2.0");
        report.setLanguage("en");

        RiskFactor factor = new RiskFactor("DTI", Severity.MEDIO, "Moderate DTI");
        report.setRiskFactors(List.of(factor));

        ReportDTO dto = mapper.toDTO(report);

        assertEquals("REP-1", dto.getReportId());
        assertEquals("P-1", dto.getPartyId());
        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("SCO-1", dto.getScoringId());
        assertEquals("RISK_ANALYSIS", dto.getReportType());
        assertEquals("Title", dto.getTitle());
        assertEquals("Summary", dto.getAiSummary());
        assertEquals("Analysis", dto.getRiskAnalysis());
        assertEquals(List.of("Rec 1"), dto.getRecommendations());
        assertEquals("/path.pdf", dto.getFilePath());
        assertEquals(500, dto.getFileSizeBytes());
        assertEquals("User", dto.getGeneratedBy());
        assertEquals(now, dto.getGeneratedDate());
        assertEquals(150, dto.getGenerationTimeMs());
        assertEquals("v2.0", dto.getModelVersion());
        assertEquals("en", dto.getLanguage());

        assertEquals(1, dto.getRiskFactors().size());
        assertEquals(factor, dto.getRiskFactors().get(0));
    }
}
