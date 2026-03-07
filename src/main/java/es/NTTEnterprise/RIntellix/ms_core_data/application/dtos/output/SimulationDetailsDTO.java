package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

import java.util.Map;

/**
 * Data Transfer Object (DTO) for the detailed view of a simulation.
 * Contains the modified input values, the recalculated risk metrics,
 * the original (base) scoring results and the computed deltas for comparison.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class SimulationDetailsDTO {

    private String simulationId;
    private String scenarioName;
    private String simulationDate;
    private String requestId;
    private String baseScoringId;

    // Modified values applied in the simulation
    private Map<String, Object> formChanges;

    // Base scoring results (original scenario)
    private Double basePd;
    private Double baseLgd;
    private Double baseEad;
    private Double baseEcl;
    private String baseRiskGrade;

    // Simulated results (scenario with changes applied)
    private Double simulatedPd;
    private Double simulatedLgd;
    private Double simulatedEad;
    private Double simulatedEcl;
    private String simulatedRiskGrade;
    private String simulatedDecision;

    // Delta (comparison between base and simulated)
    private Double pdChange;
    private Double elChange;
    private String riskGradeChange;

    public SimulationDetailsDTO() {
    }

    // Getters and Setters

    public String getSimulationId() {
        return simulationId;
    }

    public void setSimulationId(String simulationId) {
        this.simulationId = simulationId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(String simulationDate) {
        this.simulationDate = simulationDate;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getBaseScoringId() {
        return baseScoringId;
    }

    public void setBaseScoringId(String baseScoringId) {
        this.baseScoringId = baseScoringId;
    }

    public Map<String, Object> getFormChanges() {
        return formChanges;
    }

    public void setFormChanges(Map<String, Object> formChanges) {
        this.formChanges = formChanges;
    }

    public Double getBasePd() {
        return basePd;
    }

    public void setBasePd(Double basePd) {
        this.basePd = basePd;
    }

    public Double getBaseLgd() {
        return baseLgd;
    }

    public void setBaseLgd(Double baseLgd) {
        this.baseLgd = baseLgd;
    }

    public Double getBaseEad() {
        return baseEad;
    }

    public void setBaseEad(Double baseEad) {
        this.baseEad = baseEad;
    }

    public Double getBaseEcl() {
        return baseEcl;
    }

    public void setBaseEcl(Double baseEcl) {
        this.baseEcl = baseEcl;
    }

    public String getBaseRiskGrade() {
        return baseRiskGrade;
    }

    public void setBaseRiskGrade(String baseRiskGrade) {
        this.baseRiskGrade = baseRiskGrade;
    }

    public Double getSimulatedPd() {
        return simulatedPd;
    }

    public void setSimulatedPd(Double simulatedPd) {
        this.simulatedPd = simulatedPd;
    }

    public Double getSimulatedLgd() {
        return simulatedLgd;
    }

    public void setSimulatedLgd(Double simulatedLgd) {
        this.simulatedLgd = simulatedLgd;
    }

    public Double getSimulatedEad() {
        return simulatedEad;
    }

    public void setSimulatedEad(Double simulatedEad) {
        this.simulatedEad = simulatedEad;
    }

    public Double getSimulatedEcl() {
        return simulatedEcl;
    }

    public void setSimulatedEcl(Double simulatedEcl) {
        this.simulatedEcl = simulatedEcl;
    }

    public String getSimulatedRiskGrade() {
        return simulatedRiskGrade;
    }

    public void setSimulatedRiskGrade(String simulatedRiskGrade) {
        this.simulatedRiskGrade = simulatedRiskGrade;
    }

    public String getSimulatedDecision() {
        return simulatedDecision;
    }

    public void setSimulatedDecision(String simulatedDecision) {
        this.simulatedDecision = simulatedDecision;
    }

    public Double getPdChange() {
        return pdChange;
    }

    public void setPdChange(Double pdChange) {
        this.pdChange = pdChange;
    }

    public Double getElChange() {
        return elChange;
    }

    public void setElChange(Double elChange) {
        this.elChange = elChange;
    }

    public String getRiskGradeChange() {
        return riskGradeChange;
    }

    public void setRiskGradeChange(String riskGradeChange) {
        this.riskGradeChange = riskGradeChange;
    }
}
