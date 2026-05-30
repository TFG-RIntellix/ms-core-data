package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Input DTO for the POST endpoint that creates a new simulation.
 * Contains all required fields to persist a simulation: scenario name,
 * request and party references, form changes, simulated results and deltas.
 * This DTO is used when the user has completed the simulation workflow
 * (modifying parameters, recalculating) and decides to save the final result.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-08-2026
 */
public class CreateSimulationDTO {

    @NotBlank(message = "Scenario name must be provided")
    private String scenarioName;

    @NotBlank(message = "Request ID must be provided")
    private String requestId;

    @NotBlank(message = "Party ID must be provided")
    private String partyId;

    @NotBlank(message = "Base scoring ID must be provided")
    private String baseScoringId;

    // Modified input values
    @NotEmpty(message = "Form changes must be provided and not empty")
    private Map<String, Object> formChanges;

    // Simulated results (recalculated)
    @NotNull(message = "Simulated PD must be provided")
    private Double simulatedPd;

    @NotNull(message = "Simulated LGD must be provided")
    private Double simulatedLgd;

    @NotNull(message = "Simulated EAD must be provided")
    private Double simulatedEad;

    @NotNull(message = "Simulated ECL must be provided")
    private Double simulatedEcl;

    @NotBlank(message = "Simulated risk grade must be provided")
    private String simulatedRiskGrade;

    @NotBlank(message = "Simulated decision must be provided")
    private String simulatedDecision;

    // Delta (comparison with base scoring)
    @NotNull(message = "PD change must be provided")
    private Double pdChange;

    @NotNull(message = "EL change must be provided")
    private Double elChange;

    @NotBlank(message = "Risk grade change must be provided")
    private String riskGradeChange;

    public CreateSimulationDTO() {
    }

    // Getters and Setters

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getPartyId() {
        return partyId;
    }

    public void setPartyId(String partyId) {
        this.partyId = partyId;
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
