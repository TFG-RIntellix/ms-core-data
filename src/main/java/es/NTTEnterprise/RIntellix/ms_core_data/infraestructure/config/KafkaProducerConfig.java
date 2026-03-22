package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
 * messages to Kafka topics. This template is used by ScoringKafkaProducer
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

    private final String bootstrapServers;
    private final String acks;
    private final int retries;
    private final int lingerMs;

    public KafkaProducerConfig(
            @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
            @Value("${spring.kafka.producer.acks}") String acks,
            @Value("${spring.kafka.producer.retries}") int retries,
            @Value("${spring.kafka.producer.properties.linger.ms}") int lingerMs) {
        this.bootstrapServers = Objects.requireNonNull(bootstrapServers);
        this.acks = Objects.requireNonNull(acks);
        this.retries = retries;
        this.lingerMs = lingerMs;
    }

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
        configProps.put(ProducerConfig.ACKS_CONFIG, acks);
        configProps.put(ProducerConfig.RETRIES_CONFIG, retries);
        configProps.put(ProducerConfig.LINGER_MS_CONFIG, lingerMs);

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
