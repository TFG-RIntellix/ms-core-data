package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.FinancialMetricsEntity;

import java.util.HashMap;
import java.util.Map;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Simulation;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.SimulationEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.DeltaEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.FormChangesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.SimulatedResultsEntity;

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
        simulation.setSimulatedDecision(entity.getSimulatedResults() != null ? entity.getSimulatedResults().getDecision() : null);

        // Delta fields
        if (entity.getDelta() != null) {
            simulation.setPdChange(entity.getDelta().getPdChange());
            simulation.setEclChange(entity.getDelta().getEclChange());
            simulation.setRiskGradeChange(entity.getDelta().getRiskGradeChange());
            simulation.setMonthlyPaymentChange(entity.getDelta().getMonthlyPaymentChange());
            simulation.setDtiChange(entity.getDelta().getDtiChange());
            simulation.setTotalPaymentChange(entity.getDelta().getTotalPaymentChange());
            simulation.setTotalInterestChange(entity.getDelta().getTotalInterestChange());
            simulation.setMonthlyDisposableIncomeChange(entity.getDelta().getMonthlyDisposableIncomeChange());
        }

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
        delta.setEclChange(simulation.getEclChange());
        delta.setRiskGradeChange(simulation.getRiskGradeChange());
        delta.setMonthlyPaymentChange(simulation.getMonthlyPaymentChange());
        delta.setDtiChange(simulation.getDtiChange());
        delta.setTotalPaymentChange(simulation.getTotalPaymentChange());
        delta.setTotalInterestChange(simulation.getTotalInterestChange());
        delta.setMonthlyDisposableIncomeChange(simulation.getMonthlyDisposableIncomeChange());
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
        if (entity == null) {
            return changes;
        }

        putIfNotNull(changes, "annualIncome", entity.getAnnualIncome());
        putIfNotNull(changes, "termMonths", entity.getTermMonths());
        putIfNotNull(changes, "loanAmount", entity.getLoanAmount());
        putIfNotNull(changes, "interestRate", entity.getInterestRate());
        putIfNotNull(changes, "nrDependants", entity.getNrDependants());
        putIfNotNull(changes, "repaymentSystem", entity.getRepaymentSystem());
        putIfNotNull(changes, "employmentStatus", entity.getEmploymentStatus());
        putIfNotNull(changes, "isRevolving", entity.getIsRevolving());
        putIfNotNull(changes, "propertyValue", entity.getPropertyValue());
        putIfNotNull(changes, "creditLimit", entity.getCreditLimit());

        return changes;
    }

    /**
     * Maps SimulatedResultsEntity to RiskMetrics domain object.
     */
    private RiskMetrics mapSimulatedResults(SimulatedResultsEntity entity) {
        if (entity == null) {
            return null;
        }
        FinancialMetrics fm = null;
        if (entity.getFinancialMetrics() != null) {
            fm = new FinancialMetrics(
                    entity.getFinancialMetrics().getMonthlyPayment(),
                    entity.getFinancialMetrics().getDebtToIncomeRatio(),
                    entity.getFinancialMetrics().getTotalPayment(),
                    entity.getFinancialMetrics().getTotalInterest(),
                    entity.getFinancialMetrics().getMonthlyDisposableIncome()
            );
        }
        return new RiskMetrics(
                entity.getPd(),
                entity.getLgd(),
                entity.getEad(),
                entity.getEcl(),
                entity.getRiskGrade(),
                fm);
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

        Object annualIncome = formChanges.containsKey("annualIncome") ? formChanges.get("annualIncome") : formChanges.get("annual_income");
        if (annualIncome instanceof Number n) {
            entity.setAnnualIncome(n.doubleValue());
        }
        Object termMonths = formChanges.containsKey("termMonths") ? formChanges.get("termMonths") : formChanges.get("term_months");
        if (termMonths instanceof Number n) {
            entity.setTermMonths(n.intValue());
        }
        Object loanAmount = formChanges.containsKey("loanAmount") ? formChanges.get("loanAmount") : (formChanges.containsKey("loan_amount") ? formChanges.get("loan_amount") : formChanges.get("amount"));
        if (loanAmount instanceof Number n) {
            entity.setLoanAmount(n.doubleValue());
        }
        Object interestRate = formChanges.containsKey("interestRate") ? formChanges.get("interestRate") : formChanges.get("interest_rate");
        if (interestRate instanceof Number n) {
            entity.setInterestRate(n.doubleValue());
        }
        Object nrDependants = formChanges.containsKey("nrDependants") ? formChanges.get("nrDependants") : formChanges.get("nr_dependants");
        if (nrDependants instanceof Number n) {
            entity.setNrDependants(n.intValue());
        }
        Object repaymentSystem = formChanges.containsKey("repaymentSystem") ? formChanges.get("repaymentSystem") : formChanges.get("repayment_system");
        if (repaymentSystem instanceof String s) {
            entity.setRepaymentSystem(s);
        }
        Object employmentStatus = formChanges.containsKey("employmentStatus") ? formChanges.get("employmentStatus") : formChanges.get("employment_status");
        if (employmentStatus instanceof String s) {
            entity.setEmploymentStatus(s);
        }
        Object isRevolving = formChanges.containsKey("isRevolving") ? formChanges.get("isRevolving") : formChanges.get("is_revolving");
        if (isRevolving instanceof Boolean b) {
            entity.setIsRevolving(b);
        }
        Object propertyValue = formChanges.containsKey("propertyValue") ? formChanges.get("propertyValue") : formChanges.get("property_value");
        if (propertyValue instanceof Number n) {
            entity.setPropertyValue(n.doubleValue());
        }
        Object creditLimit = formChanges.containsKey("creditLimit") ? formChanges.get("creditLimit") : formChanges.get("credit_limit");
        if (creditLimit instanceof Number n) {
            entity.setCreditLimit(n.doubleValue());
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
            if (metrics.getFinancialMetrics() != null) {
                entity.setFinancialMetrics(new FinancialMetricsEntity(
                        metrics.getFinancialMetrics().getMonthlyPayment(),
                        metrics.getFinancialMetrics().getDebtToIncomeRatio(),
                        metrics.getFinancialMetrics().getTotalPayment(),
                        metrics.getFinancialMetrics().getTotalInterest(),
                        metrics.getFinancialMetrics().getMonthlyDisposableIncome()
                ));
            }
        }
        entity.setDecision(decision);
        return entity;
    }
}
