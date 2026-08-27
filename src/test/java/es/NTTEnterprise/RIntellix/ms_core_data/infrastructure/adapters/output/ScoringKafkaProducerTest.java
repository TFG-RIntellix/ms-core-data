package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.Message;
import org.springframework.kafka.support.KafkaHeaders;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
class ScoringKafkaProducerTest {

    @Mock
    private KafkaTemplate<String, Object> kafkaTemplate;

    private ScoringKafkaProducer producer;

    @BeforeEach
    void setUp() {
        producer = new ScoringKafkaProducer(kafkaTemplate, "test-topic");
    }

    @Test
    @DisplayName("Should publish loan scoring generation request successfully")
    void publishScoringGenerationRequest_loan() {
        ScoringGenerationRequest request = new ScoringGenerationRequest();
        request.setRequestId("REQ-1");
        request.setRequestType("PRESTAMO");
        request.setLoanAmount(1000.0);
        request.setInterestRate(5.0);

        assertDoesNotThrow(() -> producer.publishScoringGenerationRequest(request));

        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(messageCaptor.capture());

        Message<?> message = messageCaptor.getValue();
        assertEquals("test-topic", message.getHeaders().get(KafkaHeaders.TOPIC));
        assertEquals("REQ-1", message.getHeaders().get(KafkaHeaders.KEY));
        assertEquals("REQ-1", message.getHeaders().get("X-Request-ID"));
        assertTrue(message.getHeaders().containsKey("X-Timestamp"));
        // Payload is ScoringGenerationDTO (which is created by the factory strategy)
        assertDoesNotThrow(() -> message.getPayload());
    }

    @Test
    @DisplayName("Should publish credit card scoring generation request successfully")
    void publishScoringGenerationRequest_creditCard() {
        ScoringGenerationRequest request = new ScoringGenerationRequest();
        request.setRequestId("REQ-2");
        request.setRequestType("TARJETA_CREDITO");
        request.setCreditLimit(5000.0);

        assertDoesNotThrow(() -> producer.publishScoringGenerationRequest(request));

        ArgumentCaptor<Message<?>> messageCaptor = ArgumentCaptor.forClass(Message.class);
        verify(kafkaTemplate).send(messageCaptor.capture());

        Message<?> message = messageCaptor.getValue();
        assertEquals("test-topic", message.getHeaders().get(KafkaHeaders.TOPIC));
        assertEquals("REQ-2", message.getHeaders().get(KafkaHeaders.KEY));
    }

    @Test
    @DisplayName("Should throw exception when kafka send fails")
    void publishScoringGenerationRequest_fails() {
        ScoringGenerationRequest request = new ScoringGenerationRequest();
        request.setRequestId("REQ-1");
        request.setRequestType("PRESTAMO");

        doThrow(new RuntimeException("Kafka error")).when(kafkaTemplate).send(any(Message.class));

        assertThrows(RuntimeException.class, () -> producer.publishScoringGenerationRequest(request));
    }
}
