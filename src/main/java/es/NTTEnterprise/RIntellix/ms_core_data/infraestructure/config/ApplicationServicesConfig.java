package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestDetailsDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.RequestSummaryDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.ScoringGenerationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.mappers.SimulationDTOMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringGenerationPort;
import es.NTTEnterprise.RIntellix.ms_core_data.application.usecases.RequestApplicationService;
import es.NTTEnterprise.RIntellix.ms_core_data.application.usecases.ScoringApplicationService;
import es.NTTEnterprise.RIntellix.ms_core_data.application.usecases.ScoringConsumerService;
import es.NTTEnterprise.RIntellix.ms_core_data.application.usecases.ScoringGenerationService;
import es.NTTEnterprise.RIntellix.ms_core_data.application.usecases.SimulationApplicationService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.RequestPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ScoringPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.SimulationPortRepository;

@Configuration
public class ApplicationServicesConfig {

    @Bean
    public ScoringConsumerService scoringConsumerService(ScoringPortRepository scoringPortRepository) {
        return new ScoringConsumerService(scoringPortRepository);
    }

    @Bean
    public ScoringGenerationService scoringGenerationService(
            ScoringPortRepository scoringPortRepository,
            PartyPortRepository partyPortRepository,
            ScoringGenerationDTOMapper scoringGenerationDTOMapper,
            ScoringGenerationPort scoringGenerationPort) {
        return new ScoringGenerationService(scoringPortRepository, partyPortRepository, scoringGenerationDTOMapper,
                scoringGenerationPort);
    }

    @Bean
    public SimulationApplicationService simulationApplicationService(
            SimulationPortRepository simulationPortRepository,
            PartyPortRepository partyPortRepository,
            ScoringPortRepository scoringPortRepository,
            RequestPortRepository requestPortRepository,
            SimulationDTOMapper simulationDTOMapper) {
        return new SimulationApplicationService(simulationPortRepository, partyPortRepository, scoringPortRepository,
                requestPortRepository, simulationDTOMapper);
    }

    @Bean
    public ScoringApplicationService scoringApplicationService(
            ScoringPortRepository scoringPortRepository,
            PartyPortRepository partyPortRepository,
            RequestPortRepository requestPortRepository,
            ScoringDTOMapper scoringDTOMapper) {
        return new ScoringApplicationService(scoringPortRepository, scoringDTOMapper);
    }

    @Bean
    public RequestApplicationService requestApplicationService(
            RequestPortRepository requestPortRepository,
            PartyPortRepository partyPortRepository,
            RequestSummaryDTOMapper requestSummaryDTOMapper,
            RequestDetailsDTOMapper requestDetailsDTOMapper,
            ScoringGenerationService scoringGenerationService) {
        return new RequestApplicationService(requestPortRepository, partyPortRepository, requestSummaryDTOMapper,
                requestDetailsDTOMapper, scoringGenerationService);
    }
}
