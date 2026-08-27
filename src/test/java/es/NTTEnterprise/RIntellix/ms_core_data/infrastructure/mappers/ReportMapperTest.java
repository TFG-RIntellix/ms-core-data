package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFactor;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ReportType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Severity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ReportEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.RiskFactorEntity;

class ReportMapperTest {

    private final ReportMapper mapper = new ReportMapper();

    @Test
    @DisplayName("Should map to null correctly")
    void nullMapping() {
        assertNull(mapper.toEntity(null));
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map Report domain to ReportEntity")
    void toEntity_success() {
        Report report = new Report();
        ObjectId id = new ObjectId();
        ObjectId partyId = new ObjectId();
        report.setId(id.toHexString());
        report.setPartyId(partyId.toHexString());
        report.setReportType(ReportType.RISK_ANALYSIS);
        report.setTitle("Title");
        report.setRiskFactors(List.of(new RiskFactor("Factor 1", Severity.ALTO, "Desc")));

        ReportEntity entity = mapper.toEntity(report);

        assertEquals(id, entity.getId());
        assertEquals(partyId, entity.getPartyId());
        assertEquals("RISK_ANALYSIS", entity.getReportType());
        assertEquals("Title", entity.getTitle());
        assertEquals(1, entity.getRiskFactors().size());
        assertEquals("ALTO", entity.getRiskFactors().get(0).getSeverity());
    }

    @Test
    @DisplayName("Should map ReportEntity to Report domain")
    void toDomain_success() {
        ReportEntity entity = new ReportEntity();
        ObjectId id = new ObjectId();
        entity.setId(id);
        entity.setReportType("RISK_ANALYSIS");
        entity.setTitle("Title");
        RiskFactorEntity rfe = new RiskFactorEntity();
        rfe.setSeverity("ALTO");
        rfe.setFactor("F1");
        entity.setRiskFactors(List.of(rfe));

        Report report = mapper.toDomain(entity);

        assertEquals(id.toHexString(), report.getId());
        assertEquals(ReportType.RISK_ANALYSIS, report.getReportType());
        assertEquals(1, report.getRiskFactors().size());
        assertEquals(Severity.ALTO, report.getRiskFactors().get(0).getSeverity());
    }
}
