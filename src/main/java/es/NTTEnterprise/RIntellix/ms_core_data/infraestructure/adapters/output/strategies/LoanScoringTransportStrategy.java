package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output.strategies;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ScoringGenerationTransportDTOMapper;

/**
 * Strategy implementation for Loan and Mortgage scoring generation transport.
 *
 * Handles the transformation and message building for all fields required by
 * the scoring engine for loan and mortgage requests. Both PRESTAMO (Loan) and
 * HIPOTECA (Mortgage) use the same comprehensive feature set.
 *
 * Features included:
 * - Complete socio-demographic profile
 * - Detailed employment and financial information
 * - Full loan/mortgage parameters (term, interest rate, LTV, DTI)
 * - Credit history metrics
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public class LoanScoringTransportStrategy implements ScoringTransportStrategy {

        private static final String HEADER_REQUEST_ID = "X-Request-ID";
        private static final String HEADER_TIMESTAMP = "X-Timestamp";

        @Override
        public Message<?> buildScoreGenerationMessage(ScoringGenerationRequest scoringGenerationRequest,
                        String kafkaTopic) {
                // Map domain entity to transport DTO with all loan-specific fields
                ScoringGenerationDTO scoringGenerationDTO = ScoringGenerationTransportDTOMapper
                                .toTransportDTO(scoringGenerationRequest);

                // Build Kafka message with headers for tracking and routing
                Message<ScoringGenerationDTO> message = MessageBuilder
                                .withPayload(scoringGenerationDTO)
                                .setHeader(KafkaHeaders.TOPIC, kafkaTopic)
                                .setHeader(KafkaHeaders.KEY, scoringGenerationDTO.getRequestId())
                                .setHeader(HEADER_REQUEST_ID, scoringGenerationDTO.getRequestId())
                                .setHeader(HEADER_TIMESTAMP, System.currentTimeMillis())
                                .build();

                return message;
        }

}
