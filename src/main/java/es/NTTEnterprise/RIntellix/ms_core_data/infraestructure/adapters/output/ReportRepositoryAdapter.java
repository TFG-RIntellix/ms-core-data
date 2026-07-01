package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.Objects;

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
}
