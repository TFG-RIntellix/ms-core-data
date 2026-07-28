package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Person;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;

class SimulationDTOMapperTest {

    private final SimulationDTOMapper mapper = new SimulationDTOMapper();

    @Test
    @DisplayName("Should map to null when Simulation is null")
    void toSummaryDTO_null() {
        assertNull(mapper.toSummaryDTO(null));
        assertNull(mapper.toDetailsDTO(null, null));
    }

    @Test
    @DisplayName("Should map Simulation to SimulationSummaryDTO successfully")
    void toSummaryDTO_success() {
        Simulation sim = new Simulation();
        sim.setId("SIM-1");
        sim.setScenarioName("Scenario 1");
        sim.setRequestId("REQ-1");
        sim.setSimulationDate(new Date());
        sim.setArchived(false);

        Party party = new Party();
        Person pd = new Person();
        pd.setFirstName("John");
        pd.setLastName("Doe");
        party.setPersonDetails(pd);
        sim.setParty(party);

        SimulationSummaryDTO dto = mapper.toSummaryDTO(sim);

        assertEquals("SIM-1", dto.getSimulationId());
        assertEquals("Scenario 1", dto.getScenarioName());
        assertEquals("REQ-1", dto.getRequestId());
        assertNotNull(dto.getSimulationDate());
        assertEquals("John Doe", dto.getPartyName());
        assertFalse(dto.getIsArchived());
    }

    @Test
    @DisplayName("Should map Simulation and base Scoring to SimulationDetailsDTO successfully")
    void toDetailsDTO_success() {
        Simulation sim = new Simulation();
        sim.setId("SIM-2");
        sim.setScenarioName("Scenario 2");
        sim.setSimulationDate(new Date());
        sim.setRequestId("REQ-2");
        sim.setBaseScoringId("SCO-1");
        HashMap<String, Object> formChanges = new HashMap<>();
        formChanges.put("loanAmount", 20000.0);
        sim.setFormChanges(formChanges);
        sim.setSimulatedDecision("APPROVED");

        RiskMetrics simMetrics = new RiskMetrics();
        simMetrics.setProbabilityOfDefault(0.04);
        simMetrics.setLossGivenDefault(0.40);
        simMetrics.setExposureAtDefault(20000.0);
        simMetrics.setExpectedCalculatedLoss(320.0);
        simMetrics.setRiskLevel("LOW");
        FinancialMetrics simFin = new FinancialMetrics();
        simFin.setMonthlyPayment(600.0);
        simMetrics.setFinancialMetrics(simFin);
        sim.setSimulatedResults(simMetrics);

        sim.setPdChange(-0.01);
        sim.setEclChange(-10.0);
        sim.setRiskGradeChange("LOW -> LOW");
        sim.setMonthlyPaymentChange(50.0);

        Scoring baseScoring = new Scoring();
        RiskMetrics baseMetrics = new RiskMetrics();
        baseMetrics.setProbabilityOfDefault(0.05);
        baseMetrics.setLossGivenDefault(0.45);
        baseMetrics.setExposureAtDefault(15000.0);
        baseMetrics.setExpectedCalculatedLoss(330.0);
        baseMetrics.setRiskLevel("LOW");
        baseScoring.setResults(baseMetrics);

        SimulationDetailsDTO dto = mapper.toDetailsDTO(sim, baseScoring);

        assertEquals("SIM-2", dto.getSimulationId());
        assertEquals("Scenario 2", dto.getScenarioName());
        assertEquals("REQ-2", dto.getRequestId());
        assertEquals("SCO-1", dto.getBaseScoringId());
        
        assertEquals(20000.0, dto.getFormChanges().get("loanAmount"));
        
        assertEquals(0.04, dto.getSimulatedPd());
        assertEquals(0.40, dto.getSimulatedLgd());
        assertEquals("APPROVED", dto.getSimulatedDecision());

        assertEquals(0.05, dto.getBasePd());
        assertEquals(0.45, dto.getBaseLgd());

        assertEquals(-0.01, dto.getPdChange());
        assertEquals(-10.0, dto.getElChange());
        
        assertNotNull(dto.getSimulatedResults());
        assertEquals(0.04, dto.getSimulatedResults().getPd());
        assertEquals(600.0, dto.getSimulatedResults().getMonthlyPayment());

        assertNotNull(dto.getDelta());
        assertEquals(-0.01, dto.getDelta().getPdChange());
        assertEquals(-10.0, dto.getDelta().getEclChange());
        assertEquals(50.0, dto.getDelta().getMonthlyPaymentChange());
    }
}
