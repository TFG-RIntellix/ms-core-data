package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.FinancialMetricsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.InputFeaturesDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.RiskResultsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

@DisplayName("ScoringConsumerMessageMapper Tests")
class ScoringConsumerMessageMapperTest {

    @Test
    @DisplayName("Should return null if DTO is null")
    void toDomain_null() {
        assertNull(ScoringConsumerMessageMapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map ScoringResultMessageDTO with deep nulls")
    void toDomain_deepNulls() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        Scoring scoring = ScoringConsumerMessageMapper.toDomain(dto);

        assertNotNull(scoring);
        assertNotNull(scoring.getInputSnapshot());
        assertNull(scoring.getResults());
        assertNotNull(scoring.getExplainability());
        assertTrue(scoring.getExplainability().isEmpty());
    }

    @Test
    @DisplayName("Should map InputFeaturesDTO successfully")
    void toDomain_inputFeatures() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        InputFeaturesDTO input = new InputFeaturesDTO();
        input.setAge(30);
        input.setGender("Male");
        input.setHasMortgage("true");
        input.setIsRevolving("Si");
        input.setLoanType("Personal Loan");
        dto.setInputFeatures(input);

        Scoring scoring = ScoringConsumerMessageMapper.toDomain(dto);

        assertNotNull(scoring.getInputSnapshot());
        assertEquals(30, scoring.getInputSnapshot().getFeatures().get("age"));
        assertEquals("MALE", scoring.getInputSnapshot().getFeatures().get("gender"));
        assertEquals(Boolean.TRUE, scoring.getInputSnapshot().getFeatures().get("hasMortgage"));
        assertEquals(Boolean.TRUE, scoring.getInputSnapshot().getFeatures().get("isRevolving"));
        assertEquals("PERSONAL_LOAN", scoring.getInputSnapshot().getFeatures().get("loanType"));
    }

    @Test
    @DisplayName("Should map mortgage variants correctly")
    void normalizeMortgage_variants() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        InputFeaturesDTO input = new InputFeaturesDTO();
        dto.setInputFeatures(input);

        input.setHasMortgage("yes");
        assertEquals(Boolean.TRUE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage"));
        input.setHasMortgage("1");
        assertEquals(Boolean.TRUE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage"));

        input.setHasMortgage("no");
        assertEquals(Boolean.FALSE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage"));
        input.setHasMortgage("false");
        assertEquals(Boolean.FALSE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage"));
        input.setHasMortgage("0");
        assertEquals(Boolean.FALSE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage"));

        input.setHasMortgage("other");
        assertEquals(Boolean.FALSE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("hasMortgage")); // Boolean.valueOf("other")
                                                                                                                 // returns
                                                                                                                 // FALSE.
    }

    @Test
    @DisplayName("Should map isRevolving variants correctly")
    void mapIsRevolving_variants() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        InputFeaturesDTO input = new InputFeaturesDTO();
        dto.setInputFeatures(input);

        input.setIsRevolving("Si");
        assertEquals(Boolean.TRUE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("isRevolving"));

        input.setIsRevolving("No");
        assertEquals(Boolean.FALSE,
                ScoringConsumerMessageMapper.toDomain(dto).getInputSnapshot().getFeatures().get("isRevolving"));
    }

    @Test
    @DisplayName("Should map RiskResultsDTO successfully")
    void toDomain_riskResults() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        RiskResultsDTO results = new RiskResultsDTO();
        results.setPd("0.05");
        results.setEcl("200.0");
        results.setLgd(0.45);

        FinancialMetricsDTO fm = new FinancialMetricsDTO();
        fm.setMonthlyPayment(500.0);
        results.setFinancialMetrics(fm);

        dto.setResults(results);

        Scoring scoring = ScoringConsumerMessageMapper.toDomain(dto);

        assertNotNull(scoring.getResults());
        assertEquals(0.05, scoring.getResults().getProbabilityOfDefault());
        assertEquals(200.0, scoring.getResults().getExpectedCalculatedLoss());
        assertEquals(0.45, scoring.getResults().getLossGivenDefault());

        assertNotNull(scoring.getResults().getFinancialMetrics());
        assertEquals(500.0, scoring.getResults().getFinancialMetrics().getMonthlyPayment());
    }

    @Test
    @DisplayName("Should handle NumberFormatException in RiskResultsDTO")
    void toDomain_riskResults_invalidNumber() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();
        RiskResultsDTO results = new RiskResultsDTO();
        results.setPd("invalid");
        results.setEcl("invalid");
        dto.setResults(results);

        Scoring scoring = ScoringConsumerMessageMapper.toDomain(dto);

        assertNotNull(scoring.getResults());
        assertNull(scoring.getResults().getProbabilityOfDefault());
        assertNull(scoring.getResults().getExpectedCalculatedLoss());
    }

    @Test
    @DisplayName("Should map explainability filtering empty features")
    void toDomain_explainability() {
        ScoringResultMessageDTO dto = new ScoringResultMessageDTO();

        List<XAIFeatureDTO> features = new ArrayList<>();
        features.add(null);

        XAIFeatureDTO empty = new XAIFeatureDTO(); // all nulls
        features.add(empty);

        XAIFeatureDTO valid = new XAIFeatureDTO();
        valid.setFeatureName("age");
        valid.setShapValue(-0.1);
        features.add(valid);

        dto.setExplainability(features);

        Scoring scoring = ScoringConsumerMessageMapper.toDomain(dto);

        assertNotNull(scoring.getExplainability());
        assertEquals(1, scoring.getExplainability().size());
        assertEquals("age", scoring.getExplainability().get(0).getFeatureName());
        assertEquals(-0.1, scoring.getExplainability().get(0).getShapValue());
    }
}
