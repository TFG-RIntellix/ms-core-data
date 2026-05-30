package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/**
 * DTO representing the XAI (Explainable AI) section of a Kafka scoring message.
 * Contains SHAP base value and the top contributing features with their
 * contributions.
 * Validation constraints are applied directly on fields to ensure data
 * integrity.
 * Part of the ScoringConsumerMessageDTO nested structure.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class XAIExplanationDTO {

    @NotNull(message = "Base value is required")
    private Double baseValue;

    @NotNull(message = "Top features are required")
    @Valid
    private List<XAIFeatureDTO> topFeatures;

    /**
     * Default constructor for XAIExplanationDTO.
     */
    public XAIExplanationDTO() {
    }

    /**
     * Parameterized constructor for XAIExplanationDTO.
     * 
     * @param baseValue   the SHAP base value (expected value) of the model
     * @param topFeatures list of top contributing features with SHAP values
     */
    public XAIExplanationDTO(Double baseValue, List<XAIFeatureDTO> topFeatures) {
        this.baseValue = baseValue;
        this.topFeatures = topFeatures;
    }

    // Getters and Setters

    public Double getBaseValue() {
        return baseValue;
    }

    public void setBaseValue(Double baseValue) {
        this.baseValue = baseValue;
    }

    public List<XAIFeatureDTO> getTopFeatures() {
        return topFeatures;
    }

    public void setTopFeatures(List<XAIFeatureDTO> topFeatures) {
        this.topFeatures = topFeatures;
    }
}
