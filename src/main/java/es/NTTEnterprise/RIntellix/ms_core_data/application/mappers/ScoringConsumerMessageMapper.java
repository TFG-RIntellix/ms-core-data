package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.InputFeaturesDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.RiskResultsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

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
    public static Scoring toDomain(ScoringResultMessageDTO dto) {
        if (dto == null) {
            return null;
        }

        Scoring scoring = new Scoring();
        scoring.setRequestId(dto.getRequestId());
        scoring.setModelVersion(dto.getModelVersion());
        scoring.setExecutionDate(dto.getScoringDate());
        scoring.setInputSnapshot(toModelInputs(dto.getInputFeatures()));
        scoring.setResults(toRiskMetrics(dto.getResults()));
        scoring.setBaseValue(dto.getBaseValue());
        scoring.setExplainability(toRiskFeatures(dto.getExplainability()));

        return scoring;
    }

    /**
     * Converts InputFeaturesDTO to ModelInputs value object.
     * Creates a HashMap from DTO fields for flexible feature storage.
     * Normalizes values to the format expected by the MongoDB schema.
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
        features.put("gender", normalizeGender(dto.getGender()));
        features.put("marital_status", normalizeMaritalStatus(dto.getMaritalStatus()));
        features.put("education", normalizeEducation(dto.getEducation()));
        features.put("employment_status", normalizeEmploymentStatus(dto.getEmploymentStatus()));
        features.put("work_sector", dto.getWorkSector());
        features.put("nr_dependants", dto.getNrDependants());
        features.put("home_ownership", normalizeHomeOwnership(dto.getHomeOwnership()));
        features.put("has_mortgage", normalizeMortgage(dto.getHasMortgage()));
        features.put("annual_income", dto.getAnnualIncome());

        // Use the loan type from the PersistScoring message as the Mongo request type.
        features.put("request_type", normalizeRequestType(dto.getLoanType()));

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
     * Converts string values (PD and ECL) to Double for validation and storage.
     * 
     * @param dto the risk results DTO
     * @return RiskMetrics domain entity, or null if dto is null
     */
    private static RiskMetrics toRiskMetrics(RiskResultsDTO dto) {
        if (dto == null) {
            return null;
        }

        // Convert PD from String to Double
        Double pdValue = null;
        if (dto.getPd() != null && !dto.getPd().isEmpty()) {
            try {
                pdValue = Double.parseDouble(dto.getPd());
            } catch (NumberFormatException e) {
                pdValue = null;
            }
        }

        // Convert ECL from String to Double
        Double eclValue = null;
        if (dto.getEcl() != null && !dto.getEcl().isEmpty()) {
            try {
                eclValue = Double.parseDouble(dto.getEcl());
            } catch (NumberFormatException e) {
                eclValue = null;
            }
        }

        return new RiskMetrics(
                pdValue,
                dto.getLgd(),
                dto.getEad(),
                eclValue,
                dto.getRiskGrade());
    }

    /**
     * Converts XAI explanation DTOs to a list of RiskFeature domain entities.
     * Each XAIFeatureDTO is converted to a RiskFeature with SHAP value information.
     * Filters out items that have all null/empty values.
     * 
     * @param explainability the list of XAI feature DTOs
     * @return List of RiskFeature entities, or empty list if explainability is null
     */
    private static List<RiskFeature> toRiskFeatures(List<XAIFeatureDTO> explainability) {
        List<RiskFeature> features = new ArrayList<>();

        if (explainability == null) {
            return features;
        }

        for (XAIFeatureDTO xaiFeature : explainability) {
            if (xaiFeature != null && !isEmptyFeature(xaiFeature)) {
                RiskFeature riskFeature = new RiskFeature(
                        xaiFeature.getFeatureName(),
                        xaiFeature.getFeatureValue(),
                        xaiFeature.getShapValue(),
                        xaiFeature.getDescription());
                features.add(riskFeature);
            }
        }

        return features;
    }

    /**
     * Checks if a XAIFeatureDTO is empty (all fields are null or empty).
     * 
     * @param feature the XAI feature DTO
     * @return true if the feature is empty, false otherwise
     */
    private static boolean isEmptyFeature(XAIFeatureDTO feature) {
        return (feature.getFeatureName() == null || feature.getFeatureName().isEmpty())
                && (feature.getFeatureValue() == null || feature.getFeatureValue().isEmpty())
                && feature.getShapValue() == null
                && (feature.getDescription() == null || feature.getDescription().isEmpty());
    }

    /**
     * Normalizes gender value to uppercase format expected by MongoDB schema.
     * 
     * @param value the raw gender value from Kafka message
     * @return normalized uppercase gender value, or original if null
     */
    private static String normalizeGender(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase();
    }

    /**
     * Normalizes marital status value to uppercase format expected by MongoDB
     * schema.
     * 
     * @param value the raw marital status from Kafka message
     * @return normalized uppercase marital status value, or original if null
     */
    private static String normalizeMaritalStatus(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase();
    }

    /**
     * Normalizes education level value to uppercase format expected by MongoDB
     * schema.
     * 
     * @param value the raw education level from Kafka message
     * @return normalized uppercase education value, or original if null
     */
    private static String normalizeEducation(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase();
    }

    /**
     * Normalizes employment status value to uppercase format expected by MongoDB
     * schema.
     * 
     * @param value the raw employment status from Kafka message
     * @return normalized uppercase employment status value, or original if null
     */
    private static String normalizeEmploymentStatus(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase();
    }

    /**
     * Normalizes home ownership value to uppercase format expected by MongoDB
     * schema.
     * 
     * @param value the raw home ownership from Kafka message
     * @return normalized uppercase home ownership value, or original if null
     */
    private static String normalizeHomeOwnership(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase();
    }

    /**
     * Normalizes the mortgage flag to a boolean value.
     * Accepts the current boolean wire format and common string values such as
     * "true" and "false".
     *
     * @param value the raw mortgage flag from Kafka message
     * @return normalized boolean value, or null if the source value is null
     */
    private static Boolean normalizeMortgage(String value) {
        if (value == null) {
            return null;
        }

        String normalizedValue = value.trim().toLowerCase();
        if ("yes".equals(normalizedValue) || "true".equals(normalizedValue)
                || "1".equals(normalizedValue)) {
            return Boolean.TRUE;
        }

        if ("no".equals(normalizedValue) || "false".equals(normalizedValue) || "0".equals(normalizedValue)) {
            return Boolean.FALSE;
        }

        return Boolean.valueOf(normalizedValue);
    }

    /**
     * Normalizes request type value to uppercase format expected by MongoDB schema.
     * Maps incoming request type values to MongoDB schema enum values.
     * "Personal" → "PERSONAL", "Hipotecario" → "HIPOTECA", etc.
     * 
     * @param value the raw request type from Kafka message
     * @return normalized uppercase request type value, or null if value is null
     */
    private static String normalizeRequestType(String value) {
        if (value == null) {
            return null;
        }
        return value.toUpperCase().replace(" ", "_");
    }
}
