package es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringConsumerMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.ScoringValidationException;

/**
 * Input port for Kafka scoring consumer processing operations.
 * Defines the use case contract for processing incoming scoring
 * computation messages and persisting their results.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-21-2026
 */
public interface ScoringConsumerPortService {

    /**
     * Processes a scoring consumer message from Kafka.
     * Validates the message, checks idempotency, maps to domain entity,
     * and persists the scoring.
     * 
     * @param dto the scoring consumer message DTO from Kafka
     * @return the persisted scoring domain entity
     * @throws ScoringValidationException if the message fails validation
     */
    Scoring processScoringMessage(ScoringConsumerMessageDTO dto) throws ScoringValidationException;
}
