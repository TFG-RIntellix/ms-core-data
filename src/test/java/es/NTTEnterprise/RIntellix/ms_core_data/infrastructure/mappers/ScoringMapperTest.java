package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ScoringEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.CreditCardFieldsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.FinancialMetricsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.InputFeaturesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.LoanFieldsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.ResultsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.TopFeatureEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.XaiEntity;

class ScoringMapperTest {

    private final ScoringMapper mapper = new ScoringMapper();

    @Test
    @DisplayName("Should map null to null")
    void nullMappings() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should map ScoringEntity to Scoring domain successfully")
    void toDomain_success() {
        ScoringEntity entity = new ScoringEntity();
        ObjectId id = new ObjectId();
        ObjectId reqId = new ObjectId();
        entity.setId(id);
        entity.setRequestId(reqId);
        entity.setModelVersion("v1");
        entity.setScoringDate(new Date());

        InputFeaturesEntity inputs = new InputFeaturesEntity();
        inputs.setAge(30);
        inputs.setGender("MALE");
        LoanFieldsEntity lf = new LoanFieldsEntity();
        lf.setRequestedAmount(10000.0);
        inputs.setLoanFields(lf);
        CreditCardFieldsEntity ccf = new CreditCardFieldsEntity();
        ccf.setIsRevolving(true);
        inputs.setCreditCardFields(ccf);
        entity.setInputFeatures(inputs);

        ResultsEntity results = new ResultsEntity();
        results.setPd(0.05);
        results.setLgd(0.45);
        results.setEad(10000.0);
        results.setEcl(225.0);
        results.setRiskGrade("LOW");
        FinancialMetricsEntity fm = new FinancialMetricsEntity();
        fm.setMonthlyPayment(250.0);
        results.setFinancialMetrics(fm);
        entity.setResults(results);

        XaiEntity xai = new XaiEntity();
        xai.setBaseValue(1.5);
        TopFeatureEntity tfe = new TopFeatureEntity();
        tfe.setFeatureName("age");
        tfe.setFeatureValue("30");
        tfe.setShapValue(-0.2);
        xai.setTopFeatures(List.of(tfe));
        entity.setXai(xai);

        Scoring domain = mapper.toDomain(entity);

        assertEquals(id.toHexString(), domain.getId());
        assertEquals(reqId.toHexString(), domain.getRequestId());
        assertEquals("v1", domain.getModelVersion());
        
        Map<String, Object> features = domain.getInputSnapshot().getFeatures();
        assertEquals(30, features.get("age"));
        assertEquals("MALE", features.get("gender"));
        assertEquals(10000.0, features.get("requested_amount"));
        assertEquals(true, features.get("is_revolving"));

        assertEquals(0.05, domain.getResults().getProbabilityOfDefault());
        assertEquals(250.0, domain.getResults().getFinancialMetrics().getMonthlyPayment());

        assertEquals(1.5, domain.getBaseValue());
        assertEquals(1, domain.getExplainability().size());
        assertEquals("age", domain.getExplainability().get(0).getFeatureName());
    }

    @Test
    @DisplayName("Should map Scoring domain to ScoringEntity successfully")
    void toEntity_success() {
        Scoring domain = new Scoring();
        ObjectId reqId = new ObjectId();
        domain.setRequestId(reqId.toHexString());
        domain.setModelVersion("v1");
        
        HashMap<String, Object> features = new HashMap<>();
        features.put("age", 40);
        features.put("requested_amount", 50000.0);
        domain.setInputSnapshot(new ModelInputs(features));

        RiskMetrics rm = new RiskMetrics();
        rm.setProbabilityOfDefault(0.1);
        domain.setResults(rm);

        domain.setBaseValue(1.0);
        domain.setExplainability(List.of(new RiskFeature("age", "40", -0.1, "Age")));

        ScoringEntity entity = mapper.toEntity(domain);

        assertEquals(reqId, entity.getRequestId());
        assertEquals("v1", entity.getModelVersion());
        assertEquals(40, entity.getInputFeatures().getAge());
        assertEquals(50000.0, entity.getInputFeatures().getLoanFields().getRequestedAmount());
        assertEquals(0.1, entity.getResults().getPd());
        assertEquals(1.0, entity.getXai().getBaseValue());
        assertEquals(1, entity.getXai().getTopFeatures().size());
        assertEquals("age", entity.getXai().getTopFeatures().get(0).getFeatureName());
    }
}
