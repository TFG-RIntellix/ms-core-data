package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.CreateReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ReportDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestPartyDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;

/**
 * Core component: MapperConfig.
 * Encapsulates the logic and responsibilities assigned to this element
 * within the Hexagonal Architecture, ensuring separation of concerns.
 *
 * @author Lucía Fernández Mancebo
 * @date 28/07/2026
 */
@Configuration
public class MapperConfig {

    @Bean
    public CreateReportDTOMapper createReportDTOMapper() {
        return new CreateReportDTOMapper();
    }

    @Bean
    public ReportDTOMapper reportDTOMapper() {
        return new ReportDTOMapper();
    }

    @Bean
    public RequestDetailsDTOMapper requestDetailsDTOMapper() {
        return new RequestDetailsDTOMapper();
    }

    @Bean
    public RequestSummaryDTOMapper requestSummaryDTOMapper() {
        return new RequestSummaryDTOMapper();
    }

    @Bean
    public RequestPartyDTOMapper requestPartyDTOMapper() {
        return new RequestPartyDTOMapper();
    }

    @Bean
    public ScoringDTOMapper scoringDTOMapper() {
        return new ScoringDTOMapper();
    }

    @Bean
    public ScoringGenerationDTOMapper scoringGenerationDTOMapper() {
        return new ScoringGenerationDTOMapper();
    }

    @Bean
    public SimulationDTOMapper simulationDTOMapper() {
        return new SimulationDTOMapper();
    }
}
