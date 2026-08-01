package es.NTTEnterprise.RIntellix.ms_core_data.application.usecases;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.Map;
import java.util.stream.Collectors;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreateReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.PageResponseDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ReportDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PagedResult;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Report;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ReportPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ReportPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.CreateReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Implements ReportPortService, handling the business logic for persisting
 * AI-generated risk reports received from ms-reporting.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
@Slf4j
public class ReportApplicationService implements ReportPortService {

    private final ReportPortRepository reportPortRepository;
    private final RequestPortRepository requestPortRepository;
    private final CreateReportDTOMapper createReportDTOMapper;
    private final ReportDTOMapper reportDTOMapper;

    public ReportApplicationService(ReportPortRepository reportPortRepository,
            RequestPortRepository requestPortRepository,
            CreateReportDTOMapper createReportDTOMapper, ReportDTOMapper reportDTOMapper) {
        this.reportPortRepository = Objects.requireNonNull(reportPortRepository);
        this.requestPortRepository = Objects.requireNonNull(requestPortRepository);
        this.createReportDTOMapper = Objects.requireNonNull(createReportDTOMapper);
        this.reportDTOMapper = Objects.requireNonNull(reportDTOMapper);
    }

    @Override
    public String createReport(CreateReportDTO dto) throws IllegalArgumentException {
        log.debug(LogMessage.SERVICE_CREATE_REPORT_START, dto.getRequestId(), dto.getScoringId());

        Report report = createReportDTOMapper.toDomain(dto);

        Report saved = reportPortRepository.save(report);
        log.debug(LogMessage.SERVICE_CREATE_REPORT_COMPLETE, saved.getId());

        return saved.getId();
    }

    @Override
    public PageResponseDTO<ReportDTO> listReports(
            String search, int page, int size, String sortBy, String sortDir) {
        log.debug(LogMessage.SERVICE_LIST_REPORTS_START);

        // Resolve matching request IDs based on the search string
        List<String> matchingRequestIds = null;
        if (search != null && !search.isBlank()) {
            matchingRequestIds = requestPortRepository.findRequestIdsBySearch(search);
        }

        PagedResult<Report> pageResult = 
            reportPortRepository.findWithFilters(search, matchingRequestIds, page, size, sortBy, sortDir);
            
        List<Report> reports = pageResult.getContent();
        log.debug(LogMessage.SERVICE_LIST_REPORTS_RESULT, reports.size());

        Set<String> requestIds = reports.stream()
                .map(Report::getRequestId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
        Map<String, es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request> requestMap = requestPortRepository.findRequestsByIds(requestIds);

        List<ReportDTO> dtoList = reports.stream()
                .map(report -> {
                    ReportDTO dto = reportDTOMapper.toDTO(report);
                    if (dto.getRequestId() != null && requestMap.containsKey(dto.getRequestId())) {
                        dto.setRequestCode(requestMap.get(dto.getRequestId()).getRequestCode());
                    }
                    return dto;
                })
                .toList();

        return new PageResponseDTO<>(
                dtoList,
                pageResult.getTotalElements(),
                pageResult.getTotalPages(),
                pageResult.getNumber(),
                pageResult.getSize()
        );
    }

    @Override
    public ReportDTO getReportByRequestId(String requestId) throws EntityNotFoundException {
        log.debug(LogMessage.SERVICE_GET_REPORT_REQ_START, requestId);

        Report report = reportPortRepository.findByRequestId(requestId);
        log.debug(LogMessage.SERVICE_GET_REPORT_REQ_COMPLETE);

        ReportDTO dto = reportDTOMapper.toDTO(report);
        try {
            es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request req = requestPortRepository.findById(requestId);
            if (req != null) {
                dto.setRequestCode(req.getRequestCode());
            }
        } catch (EntityNotFoundException e) {
            // Ignore if request not found
        }

        return dto;
    }

    @Override
    public Report getReport(String reportId) throws EntityNotFoundException, IllegalArgumentException {

        log.debug(LogMessage.SERVICE_GET_REPORT_ID_START, reportId);

        if (reportId == null || reportId.isBlank()) {
            log.warn(LogMessage.SERVICE_GET_REPORT_ID_FAILED);
            throw new IllegalArgumentException(LogMessage.EXCEPTION_INVALID_REPORT_ID);
        }

        Report report = reportPortRepository.findById(reportId);
        log.debug(LogMessage.SERVICE_GET_REPORT_ID_COMPLETE, reportId);

        return report;
    }

}
