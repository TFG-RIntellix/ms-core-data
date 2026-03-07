package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

import java.util.Date;
import java.util.HashMap;

/**
 * Represents a simulation entity.
 * Encapsulates the details of a credit risk simulation scenario, including the
 * changes made to input features,
 * the resulting risk metrics, and the computed deltas (PD, EL and risk grade)
 * compared to the original scoring.
 * A simulation is always associated with a base scoring and its corresponding
 * request and party.
 * This class is used to store and manage the results of user-driven simulations
 * in the application.
 * 
 * @author Lucía Fernández Mancebo
 *         Date 03-03-2026
 */
public class Simulation {

    private String id;
    private String requestId; // Reference to the associated scoring request
    private String partyId; // Reference to the associated party (customer)
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

    // toString, hashCode and equals
    @Override
    public String toString() {
        return "Simulation [id=" + id + ", requestId=" + requestId + ", partyId=" + partyId + ", baseScoringId="
                + baseScoringId + ", scenarioName=" + scenarioName + ", simulationDate=" + simulationDate
                + ", formChanges=" + formChanges + ", simulatedResults=" + simulatedResults + ", simulatedDecision="
                + simulatedDecision + ", pdChange=" + pdChange + ", elChange=" + elChange + ", riskGradeChange="
                + riskGradeChange + ", party=" + party + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((id == null) ? 0 : id.hashCode());
        result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
        result = prime * result + ((partyId == null) ? 0 : partyId.hashCode());
        result = prime * result + ((baseScoringId == null) ? 0 : baseScoringId.hashCode());
        result = prime * result + ((scenarioName == null) ? 0 : scenarioName.hashCode());
        result = prime * result + ((simulationDate == null) ? 0 : simulationDate.hashCode());
        result = prime * result + ((formChanges == null) ? 0 : formChanges.hashCode());
        result = prime * result + ((simulatedResults == null) ? 0 : simulatedResults.hashCode());
        result = prime * result + ((simulatedDecision == null) ? 0 : simulatedDecision.hashCode());
        result = prime * result + ((pdChange == null) ? 0 : pdChange.hashCode());
        result = prime * result + ((elChange == null) ? 0 : elChange.hashCode());
        result = prime * result + ((riskGradeChange == null) ? 0 : riskGradeChange.hashCode());
        result = prime * result + ((party == null) ? 0 : party.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Simulation other = (Simulation) obj;
        if (id == null) {
            if (other.id != null)
                return false;
        } else if (!id.equals(other.id))
            return false;
        if (requestId == null) {
            if (other.requestId != null)
                return false;
        } else if (!requestId.equals(other.requestId))
            return false;
        if (partyId == null) {
            if (other.partyId != null)
                return false;
        } else if (!partyId.equals(other.partyId))
            return false;
        if (baseScoringId == null) {
            if (other.baseScoringId != null)
                return false;
        } else if (!baseScoringId.equals(other.baseScoringId))
            return false;
        if (scenarioName == null) {
            if (other.scenarioName != null)
                return false;
        } else if (!scenarioName.equals(other.scenarioName))
            return false;
        if (simulationDate == null) {
            if (other.simulationDate != null)
                return false;
        } else if (!simulationDate.equals(other.simulationDate))
            return false;
        if (formChanges == null) {
            if (other.formChanges != null)
                return false;
        } else if (!formChanges.equals(other.formChanges))
            return false;
        if (simulatedResults == null) {
            if (other.simulatedResults != null)
                return false;
        } else if (!simulatedResults.equals(other.simulatedResults))
            return false;
        if (simulatedDecision == null) {
            if (other.simulatedDecision != null)
                return false;
        } else if (!simulatedDecision.equals(other.simulatedDecision))
            return false;
        if (pdChange == null) {
            if (other.pdChange != null)
                return false;
        } else if (!pdChange.equals(other.pdChange))
            return false;
        if (elChange == null) {
            if (other.elChange != null)
                return false;
        } else if (!elChange.equals(other.elChange))
            return false;
        if (riskGradeChange == null) {
            if (other.riskGradeChange != null)
                return false;
        } else if (!riskGradeChange.equals(other.riskGradeChange))
            return false;
        if (party == null) {
            if (other.party != null)
                return false;
        } else if (!party.equals(other.party))
            return false;
        return true;
    }
}
