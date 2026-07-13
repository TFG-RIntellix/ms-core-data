package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.List;
import java.util.Objects;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ReportEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ReportMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.ReportRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Infrastructure adapter that implements the ReportPortRepository output port.
 * Delegates persistence operations to the Spring Data ReportRepository and uses
 * ReportMapper to convert between entities and domain objects.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
@Slf4j
@Repository
public class ReportRepositoryAdapter implements ReportPortRepository {

    private final ReportRepository reportRepository;
    private final ReportMapper reportMapper;

    public ReportRepositoryAdapter(ReportRepository reportRepository, ReportMapper reportMapper) {
        this.reportRepository = Objects.requireNonNull(reportRepository);
        this.reportMapper = Objects.requireNonNull(reportMapper);
    }

    @Override
    public Report save(Report report) {
        log.debug(LogMessage.REPOSITORY_REPORT_SAVE_START, report.getRequestId(), report.getScoringId());

        ReportEntity entity = reportMapper.toEntity(report);
        ReportEntity savedEntity = reportRepository.save(entity);

        log.debug(LogMessage.REPOSITORY_REPORT_SAVE_COMPLETE, savedEntity.getId());
        return reportMapper.toDomain(savedEntity);
    }

    @Override
    public List<Report> findAll() {
        log.debug(LogMessage.REPOSITORY_REPORT_FIND_ALL_START);

        List<ReportEntity> entities = reportRepository.findAll();

        log.debug(LogMessage.REPOSITORY_REPORT_FIND_ALL_COMPLETE, entities.size());
        return entities.stream().map(reportMapper::toDomain).toList();
    }

    @Override
    public List<Report> findWithFilters(String requestId, String scoringId) {
        log.debug("Executing findWithFilters on reports - requestId: [{}], scoringId: [{}]", requestId, scoringId);

        if (requestId == null && scoringId == null) {
            return findAll();
        }

        ObjectId requestOid = null;
        if (requestId != null && !requestId.trim().isEmpty()) {
            try {
                requestOid = new ObjectId(requestId);
            } catch (IllegalArgumentException e) {
                log.debug("Invalid ObjectId format for requestId: {}", requestId);
            }
        }

        ObjectId scoringOid = null;
        if (scoringId != null && !scoringId.trim().isEmpty()) {
            try {
                scoringOid = new ObjectId(scoringId);
            } catch (IllegalArgumentException e) {
                log.debug("Invalid ObjectId format for scoringId: {}", scoringId);
            }
        }

        // If filters were provided but they were not valid ObjectIds, we can either
        // return empty or search with null.
        // Assuming if they type a non-ObjectId string, no document will match.
        if (requestId != null && requestOid == null && scoringId != null && scoringOid == null) {
            return List.of();
        }

        List<ReportEntity> entities;
        
        // If the frontend sends the same ID for both, it's an OR search (generic search box)
        if (requestId != null && requestId.equals(scoringId) && requestOid != null) {
            entities = reportRepository.findByRequestIdOrScoringId(requestOid, requestOid);
        } else {
            entities = reportRepository.findWithFilters(requestOid, scoringOid);
        }
        
        log.debug("findWithFilters on reports completed - Retrieved {} entity(ies)", entities.size());
        return entities.stream().map(reportMapper::toDomain).toList();
    }

    @Override
    public Report findById(String id) throws es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException, IllegalArgumentException {
        log.debug("Executing findById on reports - id: [{}]", id);

        java.util.Optional<ReportEntity> entityOpt = reportRepository.findById(new org.bson.types.ObjectId(id));

        if (entityOpt.isEmpty()) {
            log.warn("Report not found for id: [{}]", id);
            throw new es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException("Report not found with id: " + id);
        }

        log.debug("findById on reports completed - Retrieved entity");
        return reportMapper.toDomain(entityOpt.get());
    }
}
