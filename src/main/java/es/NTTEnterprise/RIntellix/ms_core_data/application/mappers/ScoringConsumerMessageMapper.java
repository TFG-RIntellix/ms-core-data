package es.NTTEnterprise.RIntellix.ms_core_data.application.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.FinancialMetricsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.InputFeaturesDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.RiskResultsDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.XAIFeatureDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

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
        throw new UnsupportedOperationException(LogMessage.EXCEPTION_MAPPER_NEVER_INSTANTIATE);
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
        // Common features across loans and credit cards.
        features.put("age", dto.getAge());
        features.put("gender", normalizeValue(dto.getGender()));
        features.put("marital_status", normalizeValue(dto.getMaritalStatus()));
        features.put("employment_status", normalizeValue(dto.getEmploymentStatus()));
        features.put("nr_dependants", dto.getNrDependants());
        features.put("home_ownership", normalizeValue(dto.getHomeOwnership()));
        features.put("has_mortgage", normalizeMortgage(dto.getHasMortgage()));
        features.put("annual_income", dto.getAnnualIncome());
        features.put("interest_rate", dto.getInterestRate());
        features.put("dti", dto.getDti());
        features.put("previous_defaults_count", dto.getPreviousDefaultsCount());
        features.put("income_type", normalizeValue(dto.getIncomeType()));

        // Use the loan type from the PersistScoring message as the Mongo request type.
        features.put("loan_type", normalizeRequestType(dto.getLoanType()));
        features.put("education", normalizeValue(dto.getEducation()));
        features.put("work_sector", dto.getWorkSector());
        features.put("purpose", dto.getPurpose());
        features.put("requested_amount", dto.getRequestedAmount());
        features.put("term_months", dto.getTermMonths());
        features.put("ltv", dto.getLtv());
        features.put("previous_loans_count", dto.getPreviousLoansCount());

        // Credit card specific features
        features.put("employment_seniority_years", dto.getEmploymentSeniorityYears());
        features.put("lti", dto.getLti());
        features.put("requested_limit", dto.getCreditLimit());
        features.put("is_revolving", mapIsRevolving(dto.getIsRevolving()));
        features.put("existing_obligations", dto.getExistingObligations());
        return new ModelInputs(features);
    }

    /**
     * Converts RiskResultsDTO to RiskMetrics value object.
     * Maps PD, LGD, EAD, ECL, and risk grade from DTO to domain entity.
     * Also maps financial metrics if present.
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

        // Map financial metrics if present
        FinancialMetrics financialMetrics = toFinancialMetrics(dto.getFinancialMetrics());

        return new RiskMetrics(
                pdValue,
                dto.getLgd(),
                dto.getEad(),
                eclValue,
                dto.getRiskGrade(),
                financialMetrics);
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
     * Converts FinancialMetricsDTO to FinancialMetrics domain entity.
     * Maps all financial metrics fields from DTO to domain.
     *
     * @param dto the financial metrics DTO
     * @return FinancialMetrics domain entity, or null if dto is null
     */
    private static FinancialMetrics toFinancialMetrics(FinancialMetricsDTO dto) {
        if (dto == null) {
            return null;
        }

        return new FinancialMetrics(
                dto.getMonthlyPayment(),
                dto.getDebtToIncomeRatio(),
                dto.getTotalPayment(),
                dto.getTotalInterest(),
                dto.getMonthlyDisposableIncome());
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
     * Normalizes input values to uppercase format expected by MongoDB schema.
     * 
     * @param value the raw input value from Kafka message
     * @return normalized uppercase input value, or original if null
     */
    private static String normalizeValue(String value) {
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

    private static Boolean mapIsRevolving(String value) {
        if (value == null) {
            return null;
        }
        if (value.equals("Si")) {
            return Boolean.TRUE;
        } else {
            return Boolean.FALSE;
        }
    }
}
