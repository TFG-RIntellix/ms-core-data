package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import java.util.Optional;
import org.bson.types.ObjectId;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ReportEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.ReportMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.ReportRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Infrastructure adapter that implements the ReportPortRepository output port.
 * Delegates persistence operations to the Spring Data ReportRepository and uses
 * ReportMapper to convert between entities and domain objects.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
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
    public PagedResult<Report> findWithFilters(
            String search, List<String> requestIds, int page, int size, String sortBy, String sortDir) {
        log.debug(LogMessage.REPOSITORY_REPORT_FIND_ALL_START);

        String searchParam = (search != null && !search.isBlank()) ? search : "";

        List<ObjectId> requestOids = List.of();
        if (requestIds != null && !requestIds.isEmpty()) {
            requestOids = requestIds.stream()
                    .filter(ObjectId::isValid)
                    .map(ObjectId::new)
                    .toList();
        }

        Sort.Direction direction = sortDir.equalsIgnoreCase("asc") ? 
            Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable pageable = PageRequest.of(
            page, size, Sort.by(direction, sortBy));

        Page<ReportEntity> entityPage = 
            reportRepository.findWithFilters(searchParam, requestOids, pageable);

        log.debug(LogMessage.REPOSITORY_REPORT_FIND_ALL_COMPLETE, entityPage.getTotalElements());

        List<Report> domainList = entityPage.getContent().stream()
                .map(reportMapper::toDomain)
                .toList();

        return new PagedResult<>(
                domainList,
                entityPage.getTotalElements(),
                entityPage.getTotalPages(),
                entityPage.getNumber(),
                entityPage.getSize()
        );
    }

    @Override
    public Report findByRequestId(String requestId) throws EntityNotFoundException {
        log.debug(LogMessage.REPOSITORY_REPORT_FIND_BY_REQUEST_ID_START, requestId);

        if (requestId == null || requestId.trim().isEmpty()) {
            throw new IllegalArgumentException(LogMessage.EXCEPTION_INVALID_REQUEST_ID);
        }

        ObjectId requestOid;
        try {
            requestOid = new ObjectId(requestId);
        } catch (IllegalArgumentException e) {
            log.warn(LogMessage.REPOSITORY_REPORT_INVALID_OBJECT_ID, requestId);
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_REQUEST, requestId));
        }

        Optional<ReportEntity> entityOpt = reportRepository.findFirstByRequestIdOrderByGeneratedDateDesc(requestOid);

        if (entityOpt.isEmpty()) {
            log.debug(LogMessage.REPOSITORY_REPORT_NOT_FOUND_FOR_REQUEST_ID, requestId);
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_REQUEST, requestId));
        }

        log.debug(LogMessage.REPOSITORY_REPORT_FIND_BY_REQUEST_ID_COMPLETED);
        return reportMapper.toDomain(entityOpt.get());
    }

    @Override
    public Report findById(String id) throws EntityNotFoundException, IllegalArgumentException {
        log.debug(LogMessage.REPOSITORY_REPORT_FIND_BY_ID_START, id);

        Optional<ReportEntity> entityOpt = reportRepository.findById(new ObjectId(id));

        if (entityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_REPORT_NOT_FOUND_FOR_ID, id);
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_REPORT_NOT_FOUND_FOR_ID, id));
        }

        log.debug(LogMessage.REPOSITORY_REPORT_FIND_BY_ID_COMPLETED);
        return reportMapper.toDomain(entityOpt.get());
    }
}
