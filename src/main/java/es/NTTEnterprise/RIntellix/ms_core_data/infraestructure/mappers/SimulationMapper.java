package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import java.util.HashMap;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.FormChangesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.SimulatedResultsEntity;

/**
 * Mapper class to convert between SimulationEntity (infrastructure) and
 * Simulation (domain).
 * Transforms the MongoDB document structure into the clean domain model:
 * - FormChangesEntity → HashMap&lt;String, Object&gt; (flexible feature map)
 * - SimulatedResultsEntity → RiskMetrics + simulatedDecision
 * - DeltaEntity → pdChange, elChange, riskGradeChange
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Component
public class SimulationMapper {

    /**
     * Converts a SimulationEntity (infrastructure) into a Simulation domain entity.
     * 
     * @param entity The SimulationEntity from MongoDB.
     * @return The Simulation domain entity, or null if the input is null.
     */
    public Simulation toDomain(SimulationEntity entity) {
        if (entity == null) {
            return null;
        }

        Simulation simulation = new Simulation();
        simulation.setId(entity.getId());
        simulation.setRequestId(entity.getRequestId());
        simulation.setPartyId(entity.getPartyId());
        simulation.setBaseScoringId(entity.getBaseScoringId());
        simulation.setScenarioName(entity.getScenarioName());
        simulation.setSimulationDate(entity.getSimulationDate());

        // Form changes → HashMap<String, Object>
        simulation.setFormChanges(mapFormChanges(entity.getFormChanges()));

        // Simulated results → RiskMetrics + decision
        if (entity.getSimulatedResults() != null) {
            simulation.setSimulatedResults(mapSimulatedResults(entity.getSimulatedResults()));
            simulation.setSimulatedDecision(entity.getSimulatedResults().getDecision());
        }

        // Delta fields
        if (entity.getDelta() != null) {
            simulation.setPdChange(entity.getDelta().getPdChange());
            simulation.setElChange(entity.getDelta().getElChange());
            simulation.setRiskGradeChange(entity.getDelta().getRiskGradeChange());
        }

        return simulation;
    }

    // --- Private mapping methods ---

    /**
     * Maps FormChangesEntity to a HashMap of modified field name → value.
     * Each known field is placed into the map only if it is not null.
     */
    private HashMap<String, Object> mapFormChanges(FormChangesEntity entity) {
        if (entity == null) {
            return null;
        }

        HashMap<String, Object> changes = new HashMap<>();

        putIfNotNull(changes, "annual_income", entity.getAnnualIncome());
        putIfNotNull(changes, "term_months", entity.getTermMonths());
        putIfNotNull(changes, "amount", entity.getAmount());
        putIfNotNull(changes, "interest_rate", entity.getInterestRate());
        putIfNotNull(changes, "nr_dependants", entity.getNrDependants());
        putIfNotNull(changes, "repayment_system", entity.getRepaymentSystem());
        putIfNotNull(changes, "employment_status", entity.getEmploymentStatus());

        return changes;
    }

    /**
     * Maps SimulatedResultsEntity to RiskMetrics domain object.
     */
    private RiskMetrics mapSimulatedResults(SimulatedResultsEntity entity) {
        if (entity == null) {
            return null;
        }
        return new RiskMetrics(
                entity.getPd(),
                entity.getLgd(),
                entity.getEad(),
                entity.getEcl(),
                entity.getRiskGrade());
    }

    private void putIfNotNull(HashMap<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
