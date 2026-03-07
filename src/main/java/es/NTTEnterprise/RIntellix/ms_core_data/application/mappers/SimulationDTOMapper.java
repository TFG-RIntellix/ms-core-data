package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.text.SimpleDateFormat;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationDetailsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.SimulationSummaryDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;

/**
 * Mapper class to convert between Simulation (domain) and Simulation DTOs (application).
 * Handles both summary and detail conversions for the two simulation endpoints.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Component
public class SimulationDTOMapper {

    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    /**
     * Converts a Simulation domain entity into a SimulationSummaryDTO.
     * The partyName is expected to have been resolved at the application layer
     * and set on the Simulation's party field before calling this method.
     *
     * @param simulation The domain Simulation entity.
     * @return The SimulationSummaryDTO ready for the REST response.
     */
    public SimulationSummaryDTO toSummaryDTO(Simulation simulation) {
        if (simulation == null) { return null; }

        SimulationSummaryDTO dto = new SimulationSummaryDTO();
        dto.setSimulationId(simulation.getId());
        dto.setScenarioName(simulation.getScenarioName());
        dto.setRequestId(simulation.getRequestId());
        dto.setSimulationDate(simulation.getSimulationDate() != null
                ? DATE_FORMAT.format(simulation.getSimulationDate()) : null);

        // Party name (resolved at application layer)
        dto.setPartyName(simulation.getParty() != null && simulation.getParty().getPersonDetails() != null
                ? simulation.getParty().getPersonDetails().getFullName()
                : null);

        return dto;
    }

    /**
     * Converts a Simulation domain entity and its associated base Scoring into a SimulationDetailsDTO.
     * Includes modified values, simulated results, base scoring results and the computed deltas.
     *
     * @param simulation  The domain Simulation entity.
     * @param baseScoring The base Scoring entity from which the simulation was derived (may be null).
     * @return The SimulationDetailsDTO ready for the REST response.
     */
    public SimulationDetailsDTO toDetailsDTO(Simulation simulation, Scoring baseScoring) {
        if (simulation == null) { return null; }

        SimulationDetailsDTO dto = new SimulationDetailsDTO();
        dto.setSimulationId(simulation.getId());
        dto.setScenarioName(simulation.getScenarioName());
        dto.setSimulationDate(simulation.getSimulationDate() != null
                ? DATE_FORMAT.format(simulation.getSimulationDate()) : null);
        dto.setRequestId(simulation.getRequestId());
        dto.setBaseScoringId(simulation.getBaseScoringId());

        // Modified values
        dto.setFormChanges(simulation.getFormChanges());

        // Simulated results
        if (simulation.getSimulatedResults() != null) {
            dto.setSimulatedPd(simulation.getSimulatedResults().getProbabilityOfDefault());
            dto.setSimulatedLgd(simulation.getSimulatedResults().getLossGivenDefault());
            dto.setSimulatedEad(simulation.getSimulatedResults().getExposureAtDefault());
            dto.setSimulatedEcl(simulation.getSimulatedResults().getExpectedCalculatedLoss());
            dto.setSimulatedRiskGrade(simulation.getSimulatedResults().getRiskLevel());
        }
        dto.setSimulatedDecision(simulation.getSimulatedDecision());

        // Base scoring results (original scenario for comparison)
        if (baseScoring != null && baseScoring.getResults() != null) {
            dto.setBasePd(baseScoring.getResults().getProbabilityOfDefault());
            dto.setBaseLgd(baseScoring.getResults().getLossGivenDefault());
            dto.setBaseEad(baseScoring.getResults().getExposureAtDefault());
            dto.setBaseEcl(baseScoring.getResults().getExpectedCalculatedLoss());
            dto.setBaseRiskGrade(baseScoring.getResults().getRiskLevel());
        }

        // Delta (comparison)
        dto.setPdChange(simulation.getPdChange());
        dto.setElChange(simulation.getElChange());
        dto.setRiskGradeChange(simulation.getRiskGradeChange());

        return dto;
    }
}
