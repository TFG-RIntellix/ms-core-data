package es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

/**
 * Input port for Kafka scoring consumer processing operations.
 * Defines the use case contract for processing incoming scoring
 * computation messages and persisting their results.
 * 
 * @author Lucía Fernández Mancebo
 * @date 21/03/2026
 */
public interface ScoringConsumerPortService {

    /**
     * Processes a scoring consumer message from Kafka.
     * Validates the message, checks idempotency, maps to domain entity,
     * and persists the scoring.
     * 
     * @param dto the scoring consumer message DTO from Kafka
     * @return the persisted scoring domain entity
     */
    Scoring processScoringMessage(ScoringResultMessageDTO dto);
}
