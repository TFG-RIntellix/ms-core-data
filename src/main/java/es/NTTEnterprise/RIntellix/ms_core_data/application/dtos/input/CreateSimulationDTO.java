package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import jakarta.validation.Valid;

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
 * @date 08/03/2026
 */
public class CreateSimulationDTO {

    // Scenario name can be empty; the backend will generate one if not provided.
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
    @NotNull(message = "Simulated results must be provided")
    @Valid
    private SimulatedResultsInputDTO simulatedResults;

    // Delta (comparison with base scoring)
    @NotNull(message = "Delta must be provided")
    @Valid
    private DeltaInputDTO delta;

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

    public SimulatedResultsInputDTO getSimulatedResults() {
        return simulatedResults;
    }

    public void setSimulatedResults(SimulatedResultsInputDTO simulatedResults) {
        this.simulatedResults = simulatedResults;
    }

    public DeltaInputDTO getDelta() {
        return delta;
    }

    public void setDelta(DeltaInputDTO delta) {
        this.delta = delta;
    }
}
