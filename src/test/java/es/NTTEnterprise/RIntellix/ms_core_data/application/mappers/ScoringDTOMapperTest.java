package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

class ScoringDTOMapperTest {

    private final ScoringDTOMapper mapper = new ScoringDTOMapper();

    @Test
    @DisplayName("Should map null to null")
    void toDTO_null() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    @DisplayName("Should map Scoring to ScoringDTO successfully")
    void toDTO_success() {
        Scoring scoring = new Scoring();
        scoring.setId("SCO-1");
        scoring.setRequestId("REQ-1");
        scoring.setModelVersion("v1");
        scoring.setExecutionDate(new Date());

        ModelInputs inputs = new ModelInputs();
        HashMap<String, Object> features = new HashMap<>();
        features.put("is_revolving", true);
        features.put("age", 30);
        inputs.setFeatures(features);
        scoring.setInputSnapshot(inputs);

        RiskMetrics metrics = new RiskMetrics();
        metrics.setProbabilityOfDefault(0.05);
        metrics.setLossGivenDefault(0.45);
        metrics.setExposureAtDefault(10000.0);
        metrics.setExpectedCalculatedLoss(225.0);
        metrics.setRiskLevel("LOW");
        FinancialMetrics fm = new FinancialMetrics();
        fm.setMonthlyPayment(250.0);
        fm.setDebtToIncomeRatio(0.25);
        fm.setTotalPayment(12000.0);
        fm.setTotalInterest(2000.0);
        fm.setMonthlyDisposableIncome(2000.0);
        metrics.setFinancialMetrics(fm);
        scoring.setResults(metrics);

        scoring.setBaseValue(1.5);
        RiskFeature rf = new RiskFeature();
        rf.setFeatureName("age");
        rf.setFeatureValue("30");
        rf.setShapValue(-0.2);
        rf.setDescription("Reduce el riesgo de impago");
        scoring.setExplainability(List.of(rf));

        ScoringDTO dto = mapper.toDTO(scoring);

        assertEquals("SCO-1", dto.getScoringId());
        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("v1", dto.getModelVersion());
        assertNotNull(dto.getScoringDate());

        assertEquals("Si", dto.getInputFeatures().get("is_revolving"));
        assertEquals(30, dto.getInputFeatures().get("age"));

        assertEquals(0.05, dto.getPd());
        assertEquals(0.45, dto.getLgd());
        assertEquals(10000.0, dto.getEad());
        assertEquals(225.0, dto.getEcl());
        assertEquals("LOW", dto.getRiskGrade());
        assertEquals(250.0, dto.getMonthlyPayment());
        assertEquals(0.25, dto.getDti());
        assertEquals(12000.0, dto.getTotalPayment());
        assertEquals(2000.0, dto.getTotalInterest());
        assertEquals(2000.0, dto.getMonthlyDisposableIncome());

        assertEquals(1.5, dto.getBaseValue());
        assertEquals(1, dto.getTopFeatures().size());
        assertEquals("age", dto.getTopFeatures().get(0).getFeatureName());
        assertEquals("30", dto.getTopFeatures().get(0).getFeatureValue());
        assertEquals(-0.2, dto.getTopFeatures().get(0).getShapValue());
        assertEquals("Reduce el riesgo de impago", dto.getTopFeatures().get(0).getDescription());
    }

    @Test
    @DisplayName("Should map correctly when FinancialMetrics is null")
    void toDTO_nullFinancialMetrics() {
        Scoring scoring = new Scoring();
        scoring.setId("SCO-2");
        scoring.setExecutionDate(new Date());
        ModelInputs inputs = new ModelInputs();
        inputs.setFeatures(new java.util.HashMap<>());
        scoring.setInputSnapshot(inputs);
        RiskMetrics metrics = new RiskMetrics();
        metrics.setProbabilityOfDefault(0.1);
        // Do not set FinancialMetrics
        scoring.setResults(metrics);

        ScoringDTO dto = mapper.toDTO(scoring);

        assertNotNull(dto);
        assertEquals(0.1, dto.getPd());
        assertNull(dto.getMonthlyPayment());
        assertNull(dto.getDti());
        assertNull(dto.getTotalPayment());
        assertNull(dto.getTotalInterest());
        assertNull(dto.getMonthlyDisposableIncome());
    }

    @Test
    @DisplayName("Should map correctly when Results is entirely null")
    void toDTO_nullResults() {
        Scoring scoring = new Scoring();
        scoring.setId("SCO-3");
        scoring.setExecutionDate(new Date());
        ModelInputs inputs = new ModelInputs();
        inputs.setFeatures(new java.util.HashMap<>());
        scoring.setInputSnapshot(inputs);
        // Do not set Results
        
        ScoringDTO dto = mapper.toDTO(scoring);
        
        assertNotNull(dto);
        assertNull(dto.getPd());
        assertNull(dto.getDti());
    }
}
