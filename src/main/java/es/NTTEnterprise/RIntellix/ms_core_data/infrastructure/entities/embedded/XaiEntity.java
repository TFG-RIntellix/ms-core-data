package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import java.util.List;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded entity mapping the "xai" sub-document from the "scorings" MongoDB
 * collection.
 * Contains the SHAP base value and the list of top contributing features.
 *
 * @author Lucía Fernández Mancebo
 * @date 03/03/2026
 */
public class XaiEntity {

    @Field("base_value")
    private Double baseValue;

    @Field("top_features")
    private List<TopFeatureEntity> topFeatures;

    public XaiEntity() {
    }

    // Getters and Setters

    public Double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }

    public List<TopFeatureEntity> getTopFeatures() {
        return topFeatures;
    }

    public void setTopFeatures(List<TopFeatureEntity> topFeatures) {
        this.topFeatures = topFeatures;
    }
}
