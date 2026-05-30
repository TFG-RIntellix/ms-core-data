package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.DeltaEntity;
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
        simulation.setId(entity.getId().toHexString());
        simulation.setRequestId(entity.getRequestId().toHexString());
        simulation.setPartyId(entity.getPartyId().toHexString());
        simulation.setBaseScoringId(entity.getBaseScoringId() != null ? entity.getBaseScoringId().toHexString() : null);
        simulation.setScenarioName(entity.getScenarioName());
        simulation.setSimulationDate(entity.getSimulationDate());

        // Form changes → HashMap<String, Object>
        simulation.setFormChanges(mapFormChanges(entity.getFormChanges()));

        // Simulated results → RiskMetrics + decision
        simulation.setSimulatedResults(mapSimulatedResults(entity.getSimulatedResults()));
        simulation.setSimulatedDecision(entity.getSimulatedResults().getDecision());

        // Delta fields
        simulation.setPdChange(entity.getDelta().getPdChange());
        simulation.setElChange(entity.getDelta().getElChange());
        simulation.setRiskGradeChange(entity.getDelta().getRiskGradeChange());

        // Archived flag
        simulation.setArchived(entity.getIsArchived() != null ? entity.getIsArchived() : false);

        return simulation;
    }

    /**
     * Converts a Simulation domain entity into a SimulationEntity (infrastructure).
     *
     * @param simulation The Simulation domain entity.
     * @return The SimulationEntity for MongoDB, or null if the input is null.
     */
    public SimulationEntity toEntity(Simulation simulation) {
        if (simulation == null) {
            return null;
        }

        SimulationEntity entity = new SimulationEntity();
        entity.setId(simulation.getId() != null ? new ObjectId(simulation.getId()) : null);
        entity.setRequestId(simulation.getRequestId() != null ? new ObjectId(simulation.getRequestId()) : null);
        entity.setPartyId(simulation.getPartyId() != null ? new ObjectId(simulation.getPartyId()) : null);
        entity.setBaseScoringId(
                simulation.getBaseScoringId() != null ? new ObjectId(simulation.getBaseScoringId()) : null);
        entity.setScenarioName(simulation.getScenarioName());
        entity.setSimulationDate(simulation.getSimulationDate());

        // HashMap<String, Object> → FormChangesEntity
        entity.setFormChanges(mapFormChangesToEntity(simulation.getFormChanges()));

        // RiskMetrics + decision → SimulatedResultsEntity
        entity.setSimulatedResults(mapSimulatedResultsToEntity(simulation.getSimulatedResults(),
                simulation.getSimulatedDecision()));

        // Delta fields → DeltaEntity
        DeltaEntity delta = new DeltaEntity();
        delta.setPdChange(simulation.getPdChange());
        delta.setElChange(simulation.getElChange());
        delta.setRiskGradeChange(simulation.getRiskGradeChange());
        entity.setDelta(delta);

        // Archived flag
        entity.setIsArchived(simulation.isArchived());

        return entity;
    }

    // --- Private mapping methods ---

    /**
     * Maps FormChangesEntity to a HashMap of modified field name → value.
     * Each known field is placed into the map only if it is not null.
     */
    private HashMap<String, Object> mapFormChanges(FormChangesEntity entity) {
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

    /**
     * Maps a HashMap of form changes to a FormChangesEntity.
     * Known keys are mapped to their typed fields.
     */
    private FormChangesEntity mapFormChangesToEntity(Map<String, Object> formChanges) {
        FormChangesEntity entity = new FormChangesEntity();
        if (formChanges == null) {
            return entity;
        }

        if (formChanges.get("annual_income") instanceof Number n) {
            entity.setAnnualIncome(n.doubleValue());
        }
        if (formChanges.get("term_months") instanceof Number n) {
            entity.setTermMonths(n.intValue());
        }
        if (formChanges.get("amount") instanceof Number n) {
            entity.setAmount(n.doubleValue());
        }
        if (formChanges.get("interest_rate") instanceof Number n) {
            entity.setInterestRate(n.doubleValue());
        }
        if (formChanges.get("nr_dependants") instanceof Number n) {
            entity.setNrDependants(n.intValue());
        }
        if (formChanges.get("repayment_system") instanceof String s) {
            entity.setRepaymentSystem(s);
        }
        if (formChanges.get("employment_status") instanceof String s) {
            entity.setEmploymentStatus(s);
        }

        return entity;
    }

    /**
     * Maps RiskMetrics domain object and decision string to a
     * SimulatedResultsEntity.
     */
    private SimulatedResultsEntity mapSimulatedResultsToEntity(RiskMetrics metrics, String decision) {
        SimulatedResultsEntity entity = new SimulatedResultsEntity();
        if (metrics != null) {
            entity.setPd(metrics.getProbabilityOfDefault());
            entity.setLgd(metrics.getLossGivenDefault());
            entity.setEad(metrics.getExposureAtDefault());
            entity.setEcl(metrics.getExpectedCalculatedLoss());
            entity.setRiskGrade(metrics.getRiskLevel());
        }
        entity.setDecision(decision);
        return entity;
    }
}
