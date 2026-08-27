package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.data.mongodb.core.index.Indexed;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.InputFeaturesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.ResultsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded.XaiEntity;

/**
 * Infrastructure entity mapping the "scorings" MongoDB collection.
 * Represents the credit risk scoring result computed for a given request,
 * including the input feature snapshot, risk metrics and SHAP explainability.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
@Document(collection = "scorings")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ScoringEntity {

    @Id
    private ObjectId id;

    @Indexed(unique = true)
    private ObjectId requestId;
    private String modelVersion;
    private Date scoringDate;
    private InputFeaturesEntity inputFeatures;
    private ResultsEntity results;
    private XaiEntity xai;

    public ScoringEntity() {
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

    public String getModelVersion() {
        return modelVersion;
    }

    public void setModelVersion(String modelVersion) {
        this.modelVersion = modelVersion;
    }

    public Date getScoringDate() {
        return scoringDate;
    }

    public void setScoringDate(Date scoringDate) {
        this.scoringDate = scoringDate;
    }

    public InputFeaturesEntity getInputFeatures() {
        return inputFeatures;
    }

    public void setInputFeatures(InputFeaturesEntity inputFeatures) {
        this.inputFeatures = inputFeatures;
    }

    public ResultsEntity getResults() {
        return results;
    }

    public void setResults(ResultsEntity results) {
        this.results = results;
    }

    public XaiEntity getXai() {
        return xai;
    }

    public void setXai(XaiEntity xai) {
        this.xai = xai;
    }
}
