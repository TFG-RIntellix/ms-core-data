package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.DeltaEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.FormChangesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.SimulatedResultsEntity;

class SimulationMapperTest {

    private final SimulationMapper mapper = new SimulationMapper();

    @Test
    @DisplayName("Should map null to null")
    void nullMappings() {
        assertNull(mapper.toDomain(null));
        assertNull(mapper.toEntity(null));
    }

    @Test
    @DisplayName("Should map SimulationEntity to Simulation domain successfully")
    void toDomain_success() {
        SimulationEntity entity = new SimulationEntity();
        ObjectId id = new ObjectId();
        ObjectId reqId = new ObjectId();
        ObjectId partyId = new ObjectId();
        entity.setId(id);
        entity.setRequestId(reqId);
        entity.setPartyId(partyId);
        entity.setScenarioName("Scenario 1");
        entity.setSimulationDate(new Date());

        FormChangesEntity form = new FormChangesEntity();
        form.setLoanAmount(20000.0);
        form.setTermMonths(48);
        entity.setFormChanges(form);

        SimulatedResultsEntity res = new SimulatedResultsEntity();
        res.setPd(0.05);
        res.setDecision("APPROVED");
        entity.setSimulatedResults(res);

        DeltaEntity delta = new DeltaEntity();
        delta.setPdChange(-0.01);
        entity.setDelta(delta);

        entity.setIsArchived(true);

        Simulation domain = mapper.toDomain(entity);

        assertEquals(id.toHexString(), domain.getId());
        assertEquals(reqId.toHexString(), domain.getRequestId());
        assertEquals(partyId.toHexString(), domain.getPartyId());
        assertEquals("Scenario 1", domain.getScenarioName());

        assertEquals(20000.0, domain.getFormChanges().get("loanAmount"));
        assertEquals(48, domain.getFormChanges().get("termMonths"));

        assertEquals(0.05, domain.getSimulatedResults().getProbabilityOfDefault());
        assertEquals("APPROVED", domain.getSimulatedDecision());

        assertEquals(-0.01, domain.getPdChange());
        assertTrue(domain.isArchived());
    }

    @Test
    @DisplayName("Should map Simulation domain to SimulationEntity successfully")
    void toEntity_success() {
        Simulation domain = new Simulation();
        ObjectId id = new ObjectId();
        domain.setId(id.toHexString());
        domain.setScenarioName("Scenario 2");

        HashMap<String, Object> form = new HashMap<>();
        form.put("loanAmount", 30000.0);
        form.put("isRevolving", false);
        domain.setFormChanges(form);

        RiskMetrics rm = new RiskMetrics();
        rm.setProbabilityOfDefault(0.04);
        domain.setSimulatedResults(rm);
        domain.setSimulatedDecision("REJECTED");

        domain.setPdChange(0.02);
        domain.setArchived(false);

        SimulationEntity entity = mapper.toEntity(domain);

        assertEquals(id, entity.getId());
        assertEquals("Scenario 2", entity.getScenarioName());
        assertEquals(30000.0, entity.getFormChanges().getLoanAmount());
        assertEquals(false, entity.getFormChanges().getIsRevolving());

        assertEquals(0.04, entity.getSimulatedResults().getPd());
        assertEquals("REJECTED", entity.getSimulatedResults().getDecision());

        assertEquals(0.02, entity.getDelta().getPdChange());
        assertFalse(entity.getIsArchived());
    }
}
