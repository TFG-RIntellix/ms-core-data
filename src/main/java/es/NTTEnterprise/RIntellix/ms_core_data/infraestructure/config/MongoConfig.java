package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;

/**
 * MongoDB configuration for custom enum converters.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
@Configuration
public class MongoConfig {

    @Bean
    public MongoCustomConversions customConversions() {
        return new MongoCustomConversions(Arrays.asList(
            new StringToRequestTypeConverter(),
            new RequestTypeToStringConverter(),
            new StringToRequestStatusConverter(),
            new RequestStatusToStringConverter(),
            new StringToPurposeConverter(),
            new PurposeToStringConverter()
        ));
    }

    // RequestType converters
    private static class StringToRequestTypeConverter implements Converter<String, RequestType> {
        @Override
        public RequestType convert(String source) {
            return RequestType.fromMongoValue(source);
        }
    }

    private static class RequestTypeToStringConverter implements Converter<RequestType, String> {
        @Override
        public String convert(RequestType source) {
            return source.getMongoValue();
        }
    }

    // RequestStatus converters
    private static class StringToRequestStatusConverter implements Converter<String, RequestStatus> {
        @Override
        public RequestStatus convert(String source) {
            return RequestStatus.fromMongoValue(source);
        }
    }

    private static class RequestStatusToStringConverter implements Converter<RequestStatus, String> {
        @Override
        public String convert(RequestStatus source) {
            return source.getMongoValue();
        }
    }

    // Purpose converters
    private static class StringToPurposeConverter implements Converter<String, Purpose> {
        @Override
        public Purpose convert(String source) {
            return Purpose.fromMongoValue(source);
        }
    }

    private static class PurposeToStringConverter implements Converter<Purpose, String> {
        @Override
        public String convert(Purpose source) {
            return source.getMongoValue();
        }
    }
}
