package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.FinancialMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ScoringEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.CreditCardFieldsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.FinancialMetricsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.InputFeaturesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.LoanFieldsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.ResultsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.TopFeatureEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.XaiEntity;

/**
 * Mapper class to convert between ScoringEntity (infrastructure) and Scoring
 * (domain).
 * Transforms the flat MongoDB document structure into the clean domain model:
 * - InputFeaturesEntity → ModelInputs (HashMap-based feature map)
 * - ResultsEntity → RiskMetrics
 * - XaiEntity → baseValue (Double) + List<RiskFeature>
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Component
public class ScoringMapper {

    /**
     * Converts a ScoringEntity (infrastructure) into a Scoring domain entity.
     * 
     * @param entity The ScoringEntity from MongoDB.
     * @return The Scoring domain entity, or null if the input is null.
     */
    public Scoring toDomain(ScoringEntity entity) {
        if (entity == null) {
            return null;
        }

        Scoring scoring = new Scoring();
        scoring.setId(entity.getId().toHexString());
        scoring.setRequestId(entity.getRequestId().toHexString());
        scoring.setModelVersion(entity.getModelVersion());
        scoring.setExecutionDate(entity.getScoringDate());
        scoring.setInputSnapshot(mapInputFeatures(entity.getInputFeatures()));
        scoring.setResults(mapResults(entity.getResults()));

        // XAI → baseValue + explainability
        scoring.setBaseValue(entity.getXai().getBaseValue());
        scoring.setExplainability(mapTopFeatures(entity.getXai().getTopFeatures()));

        return scoring;
    }

    /**
     * Converts a Scoring domain entity into a ScoringEntity (infrastructure).
     * Transforms the clean domain model into the MongoDB document structure:
     * - ModelInputs (HashMap) → InputFeaturesEntity
     * - RiskMetrics → ResultsEntity
     * - baseValue (Double) + List<RiskFeature> → XaiEntity
     * 
     * @param domain The Scoring domain entity.
     * @return The ScoringEntity for MongoDB, or null if the input is null.
     */
    public ScoringEntity toEntity(Scoring domain) {
        if (domain == null) {
            return null;
        }

        ScoringEntity entity = new ScoringEntity();

        if (domain.getRequestId() != null) {
            entity.setRequestId(new ObjectId(domain.getRequestId()));
        }

        entity.setModelVersion(domain.getModelVersion());
        entity.setScoringDate(domain.getExecutionDate());

        // Convert ModelInputs to InputFeaturesEntity
        entity.setInputFeatures(unmapInputFeatures(domain.getInputSnapshot()));

        // Convert RiskMetrics to ResultsEntity
        entity.setResults(unmapResults(domain.getResults()));

        // Convert baseValue and explainability to XaiEntity
        entity.setXai(unmapXai(domain.getBaseValue(), domain.getExplainability()));

        return entity;
    }

    // --- Private mapping methods ---

    /**
     * Maps InputFeaturesEntity to ModelInputs (HashMap of feature name → value).
     * Each known field is placed into the map only if it is not null.
     */
    private ModelInputs mapInputFeatures(InputFeaturesEntity entity) {
        HashMap<String, Object> features = new HashMap<>();

        putIfNotNull(features, "age", entity.getAge());
        putIfNotNull(features, "gender", entity.getGender());
        putIfNotNull(features, "marital_status", entity.getMaritalStatus());
        putIfNotNull(features, "education", entity.getEducation());
        putIfNotNull(features, "employment_status", entity.getEmploymentStatus());
        putIfNotNull(features, "employment_seniority_years", entity.getEmploymentSeniorityYears());
        putIfNotNull(features, "income_type", entity.getIncomeType());
        putIfNotNull(features, "work_sector", entity.getWorkSector());
        putIfNotNull(features, "nr_dependants", entity.getNrDependants());
        putIfNotNull(features, "home_ownership", entity.getHomeOwnership());
        putIfNotNull(features, "has_mortgage", entity.getHasMortgage());
        putIfNotNull(features, "annual_income", entity.getAnnualIncome());
        putIfNotNull(features, "loan_type", entity.getLoanType());
        putIfNotNull(features, "purpose", entity.getPurpose());
        putIfNotNull(features, "interest_rate", entity.getInterestRate());
        putIfNotNull(features, "dti", entity.getDti());
        putIfNotNull(features, "existing_obligations", entity.getExistingObligations());
        putIfNotNull(features, "lti", entity.getLti());
        putIfNotNull(features, "previous_defaults_count", entity.getPreviousDefaultsCount());
        putIfNotNull(features, "product_type", entity.getProductType());

        if (entity.getLoanFields() != null) {
            putIfNotNull(features, "requested_amount", entity.getLoanFields().getRequestedAmount());
            putIfNotNull(features, "term_months", entity.getLoanFields().getTermMonths());
            putIfNotNull(features, "previous_loans_count", entity.getLoanFields().getPreviousLoansCount());
            putIfNotNull(features, "ltv", entity.getLoanFields().getLtv());
        }

        if (entity.getCreditCardFields() != null) {
            putIfNotNull(features, "requested_limit", entity.getCreditCardFields().getRequestedLimit());
            putIfNotNull(features, "is_revolving", entity.getCreditCardFields().getIsRevolving());
            putIfNotNull(features, "previous_cards_count", entity.getCreditCardFields().getPreviousCardsCount());
            putIfNotNull(features, "revolving_utilization_rate",
                    entity.getCreditCardFields().getRevolvingUtilizationRate());
        }

        return new ModelInputs(features);
    }

    /**
     * Maps ResultsEntity to RiskMetrics domain object.
     */
    private RiskMetrics mapResults(ResultsEntity entity) {
        if (entity == null) {
            return null;
        }

        FinancialMetrics financialMetrics = mapFinancialMetrics(entity.getFinancialMetrics());

        return new RiskMetrics(
                entity.getPd(),
                entity.getLgd(),
                entity.getEad(),
                entity.getEcl(),
                entity.getRiskGrade(),
                entity.getCreditLimitAssigned(),
                financialMetrics);
    }

    /**
     * Maps a list of TopFeatureEntity to a list of RiskFeature domain objects.
     */
    private List<RiskFeature> mapTopFeatures(List<TopFeatureEntity> entities) {
        if (entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(e -> new RiskFeature(e.getFeatureName(), e.getFeatureValue(), e.getShapValue(),
                        e.getDescription()))
                .collect(Collectors.toList());
    }

    /**
     * Converts ModelInputs (HashMap) to InputFeaturesEntity.
     * Extracts known feature fields from the HashMap and sets them on the entity.
     * Only non-null values are set.
     */
    private InputFeaturesEntity unmapInputFeatures(ModelInputs domain) {
        if (domain == null || domain.getFeatures() == null) {
            return new InputFeaturesEntity();
        }

        InputFeaturesEntity entity = new InputFeaturesEntity();
        HashMap<String, Object> features = domain.getFeatures();

        entity.setAge(getIntegerFeature(features, "age"));
        entity.setGender((String) features.get("gender"));
        entity.setMaritalStatus((String) features.get("marital_status"));
        entity.setEducation((String) features.get("education"));
        entity.setEmploymentStatus((String) features.get("employment_status"));
        entity.setEmploymentSeniorityYears(getIntegerFeature(features, "employment_seniority_years"));
        entity.setIncomeType((String) features.get("income_type"));
        entity.setWorkSector((String) features.get("work_sector"));
        entity.setNrDependants(getIntegerFeature(features, "nr_dependants"));
        entity.setHomeOwnership((String) features.get("home_ownership"));
        entity.setHasMortgage(getBooleanFeature(features, "has_mortgage"));
        entity.setAnnualIncome(getDoubleFeature(features, "annual_income"));
        entity.setLoanType((String) features.get("loan_type"));
        entity.setPurpose((String) features.get("purpose"));
        entity.setInterestRate(getDoubleFeature(features, "interest_rate"));
        entity.setDti(getDoubleFeature(features, "dti"));
        entity.setExistingObligations(getDoubleFeature(features, "existing_obligations"));
        entity.setLti(getDoubleFeature(features, "lti"));
        entity.setPreviousDefaultsCount(getIntegerFeature(features, "previous_defaults_count"));
        entity.setProductType((String) features.get("product_type"));

        // Loan fields mapping
        Double requestedAmount = getDoubleFeature(features, "requested_amount");
        Integer termMonths = getIntegerFeature(features, "term_months");
        Integer previousLoansCount = getIntegerFeature(features, "previous_loans_count");
        Double ltv = getDoubleFeature(features, "ltv");

        if (requestedAmount != null || termMonths != null || previousLoansCount != null || ltv != null) {
            LoanFieldsEntity loanFields = new LoanFieldsEntity();
            loanFields.setRequestedAmount(requestedAmount);
            loanFields.setTermMonths(termMonths);
            loanFields.setPreviousLoansCount(previousLoansCount);
            loanFields.setLtv(ltv);
            entity.setLoanFields(loanFields);
        }

        // Credit Card fields mapping
        Double requestedLimit = getDoubleFeature(features, "requested_limit");
        Boolean isRevolving = getBooleanFeature(features, "is_revolving");
        Integer previousCardsCount = getIntegerFeature(features, "previous_cards_count");
        Double revolvingUtilizationRate = getDoubleFeature(features, "revolving_utilization_rate");

        if (requestedLimit != null || isRevolving != null || previousCardsCount != null
                || revolvingUtilizationRate != null) {
            CreditCardFieldsEntity creditCardFields = new CreditCardFieldsEntity();
            creditCardFields.setRequestedLimit(requestedLimit);
            creditCardFields.setIsRevolving(isRevolving);
            creditCardFields.setPreviousCardsCount(previousCardsCount);
            creditCardFields.setRevolvingUtilizationRate(revolvingUtilizationRate);
            entity.setCreditCardFields(creditCardFields);
        }

        return entity;
    }

    /**
     * Converts RiskMetrics domain object to ResultsEntity.
     */
    private ResultsEntity unmapResults(RiskMetrics domain) {
        if (domain == null) {
            return new ResultsEntity();
        }

        ResultsEntity entity = new ResultsEntity();
        entity.setPd(domain.getProbabilityOfDefault());
        entity.setLgd(domain.getLossGivenDefault());
        entity.setEad(domain.getExposureAtDefault());
        entity.setEcl(domain.getExpectedCalculatedLoss());
        entity.setRiskGrade(domain.getRiskLevel());
        entity.setCreditLimitAssigned(domain.getCreditLimitAssigned());
        entity.setFinancialMetrics(unmapFinancialMetrics(domain.getFinancialMetrics()));

        return entity;
    }

    /**
     * Converts baseValue and explainability list to XaiEntity.
     * Filters out features with null/empty required fields.
     * Ensures baseValue is not null (defaults to 0.0 if needed).
     */
    private XaiEntity unmapXai(Double baseValue, List<RiskFeature> explainability) {
        XaiEntity entity = new XaiEntity();
        // MongoDB schema requires base_value: use 0.0 as default if null
        entity.setBaseValue(baseValue != null ? baseValue : 0.0);

        if (explainability == null || explainability.isEmpty()) {
            entity.setTopFeatures(new ArrayList<>());
        } else {
            List<TopFeatureEntity> topFeatures = explainability.stream()
                    .filter(e -> e != null && e.getFeatureName() != null && !e.getFeatureName().isEmpty()
                            && e.getShapValue() != null) // Only include features with required fields
                    .map(e -> {
                        TopFeatureEntity feature = new TopFeatureEntity();
                        feature.setFeatureName(e.getFeatureName());
                        feature.setFeatureValue(e.getFeatureValue() != null ? e.getFeatureValue() : "");
                        feature.setShapValue(e.getShapValue());
                        feature.setDescription(e.getDescription());
                        return feature;
                    })
                    .collect(Collectors.toList());
            entity.setTopFeatures(topFeatures);
        }

        return entity;
    }

    /**
     * Maps FinancialMetricsEntity to FinancialMetrics domain object.
     */
    private FinancialMetrics mapFinancialMetrics(FinancialMetricsEntity entity) {
        if (entity == null) {
            return null;
        }

        return new FinancialMetrics(
                entity.getMonthlyPayment(),
                entity.getDebtToIncomeRatio(),
                entity.getTotalPayment(),
                entity.getTotalInterest(),
                entity.getMonthlyDisposableIncome());
    }

    /**
     * Converts FinancialMetrics domain object to FinancialMetricsEntity.
     */
    private FinancialMetricsEntity unmapFinancialMetrics(FinancialMetrics domain) {
        if (domain == null) {
            return null;
        }

        return new FinancialMetricsEntity(
                domain.getMonthlyPayment(),
                domain.getDebtToIncomeRatio(),
                domain.getTotalPayment(),
                domain.getTotalInterest(),
                domain.getMonthlyDisposableIncome());
    }

    private void putIfNotNull(HashMap<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }

    // --- Helper methods for safe casting ---

    private Double getDoubleFeature(HashMap<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return ((Number) val).doubleValue();
        }
        return null;
    }

    private Integer getIntegerFeature(HashMap<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Number) {
            return ((Number) val).intValue();
        }
        return null;
    }

    private Boolean getBooleanFeature(HashMap<String, Object> map, String key) {
        Object val = map.get(key);
        if (val instanceof Boolean) {
            return (Boolean) val;
        } else if (val instanceof String) {
            return Boolean.parseBoolean((String) val);
        }
        return null;
    }
}
