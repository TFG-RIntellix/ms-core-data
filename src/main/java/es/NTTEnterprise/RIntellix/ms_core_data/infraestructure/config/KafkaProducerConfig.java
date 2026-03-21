package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;

/**
 * Kafka producer configuration for scoring generation messages.
 * 
 * Configures the KafkaTemplate bean for publishing ScoringGenerationDTO
 * messages to Kafka topics. This template is used by ScoringKafkaAdapter
 * to send scoring generation requests.
 * 
 * Producer settings:
 * - Bootstrap servers: configured in application.properties
 * - Key serializer: StringSerializer (for request IDs)
 * - Value serializer: JsonSerializer (for ScoringGenerationDTO)
 * - Acks: all (wait for all replicas acknowledgment)
 * - Retries: 3 (automatic retry on failure)
 * - Linger: 10ms (batch messages for efficiency)
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-15-2026
 */
@Configuration
@EnableKafka
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    /**
     * Configures the ProducerFactory for KafkaTemplate.
     * 
     * @return ProducerFactory configured with StringSerializer for keys
     *         and JacksonJsonSerializer for ScoringGenerationDTO values
     */
    @Bean
    public ProducerFactory<String, ScoringGenerationDTO> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();

        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, 10);

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    /**
     * Configures the KafkaTemplate for sending messages to Kafka.
     * 
     * @param producerFactory the producer factory bean
     * @return KafkaTemplate<String, ScoringGenerationDTO> configured and ready for
     *         use
     */
    @Bean
    public KafkaTemplate<String, ScoringGenerationDTO> kafkaTemplate(
            ProducerFactory<String, ScoringGenerationDTO> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }
}
