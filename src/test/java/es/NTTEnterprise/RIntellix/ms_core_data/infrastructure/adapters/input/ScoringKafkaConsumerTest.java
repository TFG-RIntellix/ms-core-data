package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.input;

import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.support.Acknowledgment;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringResultMessageDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.ports.input.ScoringConsumerPortService;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Scoring;

@ExtendWith(MockitoExtension.class)
class ScoringKafkaConsumerTest {

    @Mock
    private ScoringConsumerPortService scoringConsumerService;

    @Mock
    private Acknowledgment acknowledgment;

    private ScoringKafkaConsumer consumer;

    @BeforeEach
    void setUp() {
        consumer = new ScoringKafkaConsumer(scoringConsumerService);
    }

    @Test
    @DisplayName("Should consume message and acknowledge successfully")
    void consumeScoring_success() {
        ScoringResultMessageDTO message = new ScoringResultMessageDTO();
        message.setRequestId("REQ-1");

        Scoring scoring = new Scoring();
        scoring.setId("SCORE-1");

        when(scoringConsumerService.processScoringMessage(message)).thenReturn(scoring);

        consumer.consumeScoring(message, acknowledgment);

        verify(scoringConsumerService).processScoringMessage(message);
        verify(acknowledgment).acknowledge();
    }

    @Test
    @DisplayName("Should handle null message gracefully")
    void consumeScoring_nullMessage() {
        when(scoringConsumerService.processScoringMessage(null)).thenReturn(null);

        consumer.consumeScoring(null, acknowledgment);

        verify(scoringConsumerService).processScoringMessage(null);
        verify(acknowledgment).acknowledge();
    }

    @Test
    @DisplayName("Should handle null acknowledgment gracefully")
    void consumeScoring_nullAcknowledgment() {
        ScoringResultMessageDTO message = new ScoringResultMessageDTO();
        message.setRequestId("REQ-1");

        when(scoringConsumerService.processScoringMessage(message)).thenReturn(new Scoring());

        consumer.consumeScoring(message, null);

        verify(scoringConsumerService).processScoringMessage(message);
        // Should not throw NPE
    }
}
