package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ScoringEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ScoringMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.ScoringRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Infrastructure adapter that implements the ScoringPortRepository output port.
 * Delegates persistence operations to the Spring Data ScoringRepository
 * and uses ScoringMapper to convert entities to domain objects.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
@Slf4j
@Repository
public class ScoringRepositoryAdapter implements ScoringPortRepository {

    private final ScoringRepository scoringRepository;
    private final ScoringMapper scoringMapper;

    public ScoringRepositoryAdapter(ScoringRepository scoringRepository, ScoringMapper scoringMapper) {
        this.scoringRepository = scoringRepository;
        this.scoringMapper = scoringMapper;
    }

    @Override
    public Scoring findByRequestId(String requestId) throws EntityNotFoundException {
        log.debug(LogMessage.REPOSITORY_SCORING_FIND_BY_REQUEST_START, requestId);
        Optional<ScoringEntity> entityOpt = scoringRepository.findByRequestId(new ObjectId(requestId));

        if (entityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_SCORING_FIND_BY_REQUEST_NOT_FOUND, requestId);
            throw new EntityNotFoundException("Scoring not found for request ID " + requestId);
        }

        log.debug(LogMessage.REPOSITORY_SCORING_FIND_BY_REQUEST_FOUND, requestId);
        return scoringMapper.toDomain(entityOpt.get());
    }

    @Override
    public Scoring findById(String scoringId) throws EntityNotFoundException {
        log.debug(LogMessage.REPOSITORY_SCORING_FIND_BY_ID_START, scoringId);
        Optional<ScoringEntity> entityOpt = scoringRepository.findById(new ObjectId(scoringId));

        if (entityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_SCORING_FIND_BY_ID_NOT_FOUND, scoringId);
            throw new EntityNotFoundException("Scoring with ID " + scoringId + " not found");
        }

        log.debug(LogMessage.REPOSITORY_SCORING_FIND_BY_ID_FOUND, scoringId);
        return scoringMapper.toDomain(entityOpt.get());
    }
}
