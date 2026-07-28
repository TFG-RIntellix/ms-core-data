package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.DeltaEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.FormChangesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.SimulatedResultsEntity;

/**
 * Infrastructure entity mapping the "simulations" MongoDB collection.
 * Represents a what-if simulation scenario derived from a base scoring,
 * including modified inputs, recalculated risk metrics and deltas.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Document(collection = "simulations")
public class SimulationEntity {

    @Id
    private ObjectId id;

    @Field("request_id")
    private ObjectId requestId;

    @Field("party_id")
    private ObjectId partyId;

    @Field("base_scoring_id")
    private ObjectId baseScoringId;

    @Field("scenario_name")
    private String scenarioName;

    @Field("simulation_date")
    private Date simulationDate;

    @Field("form_changes")
    private FormChangesEntity formChanges;

    @Field("simulated_results")
    private SimulatedResultsEntity simulatedResults;

    @Field("delta")
    private DeltaEntity delta;

    @Field("is_archived")
    private Boolean isArchived;

    public SimulationEntity() {
    }

    // Getters and Setters

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public ObjectId getRequestId() {
        return requestId;
    }

    public void setRequestId(ObjectId requestId) {
        this.requestId = requestId;
    }

    public ObjectId getPartyId() {
        return partyId;
    }

    public void setPartyId(ObjectId partyId) {
        this.partyId = partyId;
    }

    public ObjectId getBaseScoringId() {
        return baseScoringId;
    }

    public void setBaseScoringId(ObjectId baseScoringId) {
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

    public FormChangesEntity getFormChanges() {
        return formChanges;
    }

    public void setFormChanges(FormChangesEntity formChanges) {
        this.formChanges = formChanges;
    }

    public SimulatedResultsEntity getSimulatedResults() {
        return simulatedResults;
    }

    public void setSimulatedResults(SimulatedResultsEntity simulatedResults) {
        this.simulatedResults = simulatedResults;
    }

    public DeltaEntity getDelta() {
        return delta;
    }

    public void setDelta(DeltaEntity delta) {
        this.delta = delta;
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }
}
