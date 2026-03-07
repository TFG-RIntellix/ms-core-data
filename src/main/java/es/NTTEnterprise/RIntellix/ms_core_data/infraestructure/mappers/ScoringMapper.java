package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.ModelInputs;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskFeature;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RiskMetrics;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ScoringEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.InputFeaturesEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.ResultsEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.embedded.TopFeatureEntity;

/**
 * Mapper class to convert between ScoringEntity (infrastructure) and Scoring (domain).
 * Transforms the flat MongoDB document structure into the clean domain model:
 *   - InputFeaturesEntity  → ModelInputs (HashMap-based feature map)
 *   - ResultsEntity        → RiskMetrics
 *   - XaiEntity            → baseValue (Double) + List&lt;RiskFeature&gt;
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Component
public class ScoringMapper {

    /**
     * Converts a ScoringEntity (infrastructure) into a Scoring domain entity.
     * @param entity The ScoringEntity from MongoDB.
     * @return The Scoring domain entity, or null if the input is null.
     */
    public Scoring toDomain(ScoringEntity entity) {
        if (entity == null) { return null; }

        Scoring scoring = new Scoring();
        scoring.setId(entity.getId());
        scoring.setRequestId(entity.getRequestId());
        scoring.setModelVersion(entity.getModelVersion());
        scoring.setExecutionDate(entity.getScoringDate());
        scoring.setInputSnapshot(mapInputFeatures(entity.getInputFeatures()));
        scoring.setResults(mapResults(entity.getResults()));

        // XAI → baseValue + explainability
        if (entity.getXai() != null) {
            scoring.setBaseValue(entity.getXai().getBaseValue());
            scoring.setExplainability(mapTopFeatures(entity.getXai().getTopFeatures()));
        }

        return scoring;
    }

    // --- Private mapping methods ---

    /**
     * Maps InputFeaturesEntity to ModelInputs (HashMap of feature name → value).
     * Each known field is placed into the map only if it is not null.
     */
    private ModelInputs mapInputFeatures(InputFeaturesEntity entity) {
        if (entity == null) { return null; }

        HashMap<String, Object> features = new HashMap<>();

        putIfNotNull(features, "age", entity.getAge());
        putIfNotNull(features, "gender", entity.getGender());
        putIfNotNull(features, "marital_status", entity.getMaritalStatus());
        putIfNotNull(features, "education", entity.getEducation());
        putIfNotNull(features, "employment_status", entity.getEmploymentStatus());
        putIfNotNull(features, "work_sector", entity.getWorkSector());
        putIfNotNull(features, "nr_dependants", entity.getNrDependants());
        putIfNotNull(features, "home_ownership", entity.getHomeOwnership());
        putIfNotNull(features, "has_mortgage", entity.getHasMortgage());
        putIfNotNull(features, "annual_income", entity.getAnnualIncome());
        putIfNotNull(features, "request_type", entity.getRequestType());
        putIfNotNull(features, "purpose", entity.getPurpose());
        putIfNotNull(features, "requested_amount", entity.getRequestedAmount());
        putIfNotNull(features, "term_months", entity.getTermMonths());
        putIfNotNull(features, "interest_rate", entity.getInterestRate());
        putIfNotNull(features, "ltv", entity.getLtv());
        putIfNotNull(features, "dti", entity.getDti());
        putIfNotNull(features, "previous_loans_count", entity.getPreviousLoansCount());
        putIfNotNull(features, "previous_defaults_count", entity.getPreviousDefaultsCount());

        return new ModelInputs(features);
    }

    /**
     * Maps ResultsEntity to RiskMetrics domain object.
     */
    private RiskMetrics mapResults(ResultsEntity entity) {
        if (entity == null) { return null; }
        return new RiskMetrics(
            entity.getPd(),
            entity.getLgd(),
            entity.getEad(),
            entity.getEcl(),
            entity.getRiskGrade()
        );
    }

    /**
     * Maps a list of TopFeatureEntity to a list of RiskFeature domain objects.
     */
    private List<RiskFeature> mapTopFeatures(List<TopFeatureEntity> entities) {
        if (entities == null || entities.isEmpty()) { return new ArrayList<>(); }
        return entities.stream()
                .map(e -> new RiskFeature(e.getFeatureName(), e.getFeatureValue(), e.getShapValue(), null))
                .collect(Collectors.toList());
    }

    private void putIfNotNull(HashMap<String, Object> map, String key, Object value) {
        if (value != null) {
            map.put(key, value);
        }
    }
}
