package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import java.util.Objects;


import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringConsumerMessageMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ScoringConsumerPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Application service for processing Kafka scoring consumer messages.
 * Handles validation and persistence of scoring results.
 * Implements the use case of consuming scoring computations from Kafka
 * and persisting them to MongoDB.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
@Slf4j
public class ScoringConsumerService implements ScoringConsumerPortService {

    private static final String MESSAGE_DTO_REQUIRED = "Scoring consumer message cannot be null";

    private final ScoringPortRepository scoringPortRepository;

    /**
     * Constructs a ScoringConsumerService with the required repository dependency.
     * Uses constructor injection with null safety checks.
     * 
     * @param scoringPortRepository the port for scoring persistence operations
     */
    public ScoringConsumerService(final ScoringPortRepository scoringPortRepository) {
        this.scoringPortRepository = Objects.requireNonNull(scoringPortRepository);
    }

    @Override
    public Scoring processScoringMessage(final ScoringResultMessageDTO dto) {
        final ScoringResultMessageDTO safeDto = Objects.requireNonNull(dto, MESSAGE_DTO_REQUIRED);
        String requestId = safeDto.getRequestId();
        log.info(LogMessage.SERVICE_SCORING_CONSUMER_START, requestId);
        log.info(LogMessage.SERVICE_SCORING_CONSUMER_VALIDATION, requestId,
                safeDto.getModelVersion());

        // Map DTO to domain entity
        Scoring scoring = ScoringConsumerMessageMapper.toDomain(safeDto);

        // Persist to database

        log.info(LogMessage.SERVICE_SCORING_CONSUMER_PERSIST, requestId,
                scoring.getResults().getRiskLevel(),
                scoring.getResults().getProbabilityOfDefault(),
                scoring.getResults().getLossGivenDefault());

        log.info(scoring.toString());

        Scoring persistedScoring = scoringPortRepository.save(scoring);

        log.info(LogMessage.SERVICE_SCORING_CONSUMER_COMPLETE, requestId,
                persistedScoring.getId(),
                persistedScoring.getExplainability() != null ? persistedScoring.getExplainability().size() : 0);

        return persistedScoring;
    }
}
