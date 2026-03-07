package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities;

import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.InputFeaturesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.ResultsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.XaiEntity;

/**
 * Infrastructure entity mapping the "scorings" MongoDB collection.
 * Represents the credit risk scoring result computed for a given request,
 * including the input feature snapshot, risk metrics and SHAP explainability.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Document(collection = "scorings")
public class ScoringEntity {

    @Id
    private ObjectId id;

    @Field("request_id")
    private ObjectId requestId;

    @Field("model_version")
    private String modelVersion;

    @Field("scoring_date")
    private Date scoringDate;

    @Field("input_features")
    private InputFeaturesEntity inputFeatures;

    @Field("results")
    private ResultsEntity results;

    @Field("xai")
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
