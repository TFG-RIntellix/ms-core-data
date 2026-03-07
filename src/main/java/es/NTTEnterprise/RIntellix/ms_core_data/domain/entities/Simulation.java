package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.Date;
import java.util.HashMap;

/**
 * Represents a simulation entity.
 * Encapsulates the details of a credit risk simulation scenario, including the changes made to input features,
 * the resulting risk metrics, and the computed deltas (PD, EL and risk grade) compared to the original scoring.
 * A simulation is always associated with a base scoring and its corresponding request and party.
 * This class is used to store and manage the results of user-driven simulations in the application.
 * @author Lucía Fernández Mancebo
 * Date 03-03-2026
 */
public class Simulation {

    private String id;
    private String requestId;
    private String partyId;
    private String baseScoringId;
    private String scenarioName;
    private Date simulationDate;

    private HashMap<String, Object> formChanges;
    private RiskMetrics simulatedResults;
    private String simulatedDecision;

    // Delta fields (comparison with base scoring)
    private Double pdChange;
    private Double elChange;
    private String riskGradeChange;

    // Transient: enriched party (resolved at application layer)
    private Party party;


    /**
     * Default constructor for Simulation.
     */
    public Simulation() {
    }

    /**
     * Constructor for Simulation.
     *
     * @param scenarioName   The name of the simulation scenario.
     * @param simulationDate The date when the simulation was performed.
     */
    public Simulation(String scenarioName, Date simulationDate) {
        this.scenarioName = scenarioName;
        this.simulationDate = simulationDate;
    }


    // Getters and Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public Date getSimulationDate() {
        return simulationDate;
    }

    public void setSimulationDate(Date simulationDate) {
        this.simulationDate = simulationDate;
    }

    public HashMap<String, Object> getFormChanges() {
        return formChanges;
    }

    public void setFormChanges(HashMap<String, Object> formChanges) {
        this.formChanges = formChanges;
    }

    public RiskMetrics getSimulatedResults() {
        return simulatedResults;
    }

    public void setSimulatedResults(RiskMetrics simulatedResults) {
        this.simulatedResults = simulatedResults;
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

    public Party getParty() {
        return party;
    }

    public void setParty(Party party) {
        this.party = party;
    }
}
