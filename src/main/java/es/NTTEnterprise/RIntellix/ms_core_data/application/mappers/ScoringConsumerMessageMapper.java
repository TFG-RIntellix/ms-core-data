package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.InputFeaturesDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.RiskResultsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringConsumerMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIExplanationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

/**
 * Static mapper for converting Kafka scoring consumer message DTOs to domain
 * entities.
 * Provides null-safe conversion from ScoringConsumerMessageDTO to Scoring
 * domain entity.
 * No-arg constructor is private to prevent instantiation.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public class ScoringConsumerMessageMapper {

    private ScoringConsumerMessageMapper() {
        throw new UnsupportedOperationException("Never instantiate");
    }

    /**
     * Converts a ScoringConsumerMessageDTO to a Scoring domain entity.
     * Performs null-safe conversion of all nested DTOs and collections.
     * 
     * @param dto the scoring consumer message DTO from Kafka deserialization
     * @return Scoring domain entity, or null if dto is null
     */
    public static Scoring toDomain(ScoringConsumerMessageDTO dto) {
        if (dto == null) {
            return null;
        }

        Scoring scoring = new Scoring();
        scoring.setRequestId(dto.getRequestId());
        scoring.setModelVersion(dto.getModelVersion());
        scoring.setExecutionDate(dto.getScoringDate());
        scoring.setInputSnapshot(toModelInputs(dto.getInputFeatures()));
        scoring.setResults(toRiskMetrics(dto.getResults()));
        scoring.setBaseValue(toBaseValue(dto.getXai()));
        scoring.setExplainability(toRiskFeatures(dto.getXai()));

        return scoring;
    }

    /**
     * Converts InputFeaturesDTO to ModelInputs value object.
     * Creates a HashMap from DTO fields for flexible feature storage.
     * 
     * @param dto the input features DTO
     * @return ModelInputs containing feature map, or empty ModelInputs if dto is
     *         null
     */
    private static ModelInputs toModelInputs(InputFeaturesDTO dto) {
        if (dto == null) {
            return new ModelInputs();
        }

        HashMap<String, Object> features = new HashMap<>();
        features.put("age", dto.getAge());
        features.put("gender", dto.getGender());
        features.put("marital_status", dto.getMaritalStatus());
        features.put("education", dto.getEducation());
        features.put("employment_status", dto.getEmploymentStatus());
        features.put("work_sector", dto.getWorkSector());
        features.put("nr_dependants", dto.getNrDependants());
        features.put("home_ownership", dto.getHomeOwnership());
        features.put("has_mortgage", dto.getHasMortgage());
        features.put("annual_income", dto.getAnnualIncome());

        // Convert requestType string to RequestType enum and use its JSON serialized
        // value
        if (dto.getRequestType() != null) {
            try {
                RequestType requestType = RequestType.fromValue(dto.getRequestType());
                features.put("request_type", requestType.getValue());
            } catch (IllegalArgumentException e) {
                // Fallback to original value if parsing fails
                features.put("request_type", dto.getRequestType());
            }
        }

        features.put("purpose", dto.getPurpose());
        features.put("requested_amount", dto.getRequestedAmount());
        features.put("term_months", dto.getTermMonths());
        features.put("interest_rate", dto.getInterestRate());
        features.put("ltv", dto.getLtv());
        features.put("dti", dto.getDti());
        features.put("previous_loans_count", dto.getPreviousLoansCount());
        features.put("previous_defaults_count", dto.getPreviousDefaultsCount());

        return new ModelInputs(features);
    }

    /**
     * Converts RiskResultsDTO to RiskMetrics value object.
     * Maps PD, LGD, EAD, ECL, and risk grade from DTO to domain entity.
     * 
     * @param dto the risk results DTO
     * @return RiskMetrics domain entity, or null if dto is null
     */
    private static RiskMetrics toRiskMetrics(RiskResultsDTO dto) {
        if (dto == null) {
            return null;
        }

        return new RiskMetrics(
                dto.getPd(),
                dto.getLgd(),
                dto.getEad(),
                dto.getEcl(),
                dto.getRiskGrade());
    }

    /**
     * Extracts the SHAP base value from XAI explanation DTO.
     * 
     * @param dto the XAI explanation DTO
     * @return the base value, or null if dto is null
     */
    private static Double toBaseValue(XAIExplanationDTO dto) {
        if (dto == null) {
            return null;
        }
        return dto.getBaseValue();
    }

    /**
     * Converts XAI explanation DTOs to a list of RiskFeature domain entities.
     * Each XAIFeatureDTO is converted to a RiskFeature with SHAP value information.
     * 
     * @param dto the XAI explanation DTO
     * @return List of RiskFeature entities, or empty list if dto is null
     */
    private static List<RiskFeature> toRiskFeatures(XAIExplanationDTO dto) {
        List<RiskFeature> features = new ArrayList<>();

        if (dto == null || dto.getTopFeatures() == null) {
            return features;
        }

        for (XAIFeatureDTO xaiFeature : dto.getTopFeatures()) {
            if (xaiFeature != null) {
                RiskFeature riskFeature = new RiskFeature(
                        xaiFeature.getFeatureName(),
                        xaiFeature.getFeatureValue(),
                        xaiFeature.getShapValue(),
                        null // description is not provided in the DTO
                );
                features.add(riskFeature);
            }
        }

        return features;
    }
}
