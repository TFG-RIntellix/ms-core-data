package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output;

/**
 * Data Transfer Object (DTO) representing a summary of a simulation.
 * Contains only the basic information needed for the simulations list view:
 * scenario name, associated client, linked request and creation date.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public class SimulationSummaryDTO {

    private String simulationId;
    private String scenarioName;
    private String partyName;
    private String requestId;
    private String simulationDate;

    public SimulationSummaryDTO() {
    }

    /**
     * Constructor for SimulationSummaryDTO.
     *
     * @param simulationId   the unique identifier of the simulation
     * @param scenarioName   the descriptive name of the simulation scenario
     * @param partyName      the full name of the associated client
     * @param requestId      the ID of the associated request
     * @param simulationDate the date when the simulation was created
     */
    public SimulationSummaryDTO(String simulationId, String scenarioName, String partyName,
                                String requestId, String simulationDate) {
        this.simulationId = simulationId;
        this.scenarioName = scenarioName;
        this.partyName = partyName;
        this.requestId = requestId;
        this.simulationDate = simulationDate;
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

    public String getPartyName() {
        return partyName;
    }

    public void setPartyName(String partyName) {
        this.partyName = partyName;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(String simulationDate) {
        this.simulationDate = simulationDate;
    }
}
