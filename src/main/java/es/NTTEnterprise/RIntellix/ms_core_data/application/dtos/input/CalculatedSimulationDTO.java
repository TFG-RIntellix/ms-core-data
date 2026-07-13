package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import java.util.Map;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

/**
 * Input DTO for the PUT endpoint that updates an existing persisted simulation.
 * Contains all modifiable fields of a simulation: form changes,
 * simulated results, deltas, and the associated scoring and party references.
 * Used when the user recalculates and updates a simulation that was already saved.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-07-2026
 */
public class CalculatedSimulationDTO {

    @NotBlank(message = "Party ID must be provided")
    private String partyId;

    @NotBlank(message = "Base scoring ID must be provided")
    private String baseScoringId;

    // Modified input values
    @NotEmpty(message = "Form changes must be provided and not empty")
    private Map<String, Object> formChanges;

    // Simulated results (recalculated)
    @NotNull(message = "Simulated results must be provided")
    @jakarta.validation.Valid
    private SimulatedResultsInputDTO simulatedResults;

    // Delta (comparison with base scoring)
    @NotNull(message = "Delta must be provided")
    @jakarta.validation.Valid
    private DeltaInputDTO delta;

    public CalculatedSimulationDTO() {
    }

    // Getters and Setters

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
