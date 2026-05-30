package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output.strategies;

import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreditCardScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.output.ScoringTransportStrategy;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.CreditCardScoringGenerationTransportDTOMapper;

/**
 * Strategy implementation for Credit Card scoring generation transport.
 *
 * Handles the transformation and message building for credit card specific
 * scoring requests. Credit cards require a different, more focused feature set
 * compared to loans and mortgages.
 *
 * Features included:
 * - Core socio-demographic profile (age, gender, marital status)
 * - Employment and income information
 * - Credit card-specific parameters (credit limit, revolving status)
 *
 * All credit card fields are already present in ScoringGenerationRequest,
 * populated during the mapping phase from the Request entity's RequestDetails.
 * This strategy is stateless and self-contained.
 *
 * @author Lucia Fernandez Mancebo
 * @Date 04-05-2026
 */
public class CreditCardScoringTransportStrategy implements ScoringTransportStrategy {

        private static final String HEADER_REQUEST_ID = "X-Request-ID";
        private static final String HEADER_TIMESTAMP = "X-Timestamp";

        /**
         * Constructs the strategy (stateless).
         */
        public CreditCardScoringTransportStrategy() {
        }

        @Override
        public Message<?> buildScoreGenerationMessage(ScoringGenerationRequest scoringGenerationRequest,
                        String kafkaTopic) {
                // Map domain entity to credit card transport DTO with focused fields
                CreditCardScoringGenerationDTO creditCardDTO = CreditCardScoringGenerationTransportDTOMapper
                                .toTransportDTO(scoringGenerationRequest);

                // Build Kafka message with headers for tracking and routing
                Message<CreditCardScoringGenerationDTO> message = MessageBuilder
                                .withPayload(creditCardDTO)
                                .setHeader(KafkaHeaders.TOPIC, kafkaTopic)
                                .setHeader(KafkaHeaders.KEY, creditCardDTO.getRequestId())
                                .setHeader(HEADER_REQUEST_ID, creditCardDTO.getRequestId())
                                .setHeader(HEADER_TIMESTAMP, System.currentTimeMillis())
                                .build();

                return message;
        }

}
