package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.PropertyCollateral;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.RequestDetails;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.RequestEntity;

/**
 * Mapper class to convert between RequestEntity (infrastructure) and Request
 * (domain).
 * 
 * @author Lucía Fernández Mancebo
 * @date 28/02/2026
 */
@Component
public class RequestMapper {

    /**
     * Converts a RequestEntity to a Request domain object.
     * 
     * @param entity the RequestEntity to convert
     * @return the converted Request domain object
     */
    public Request toDomain(RequestEntity entity) {
        if (entity == null) {
            return null;
        }

        // Build PropertyCollateral (only for mortgages)
        PropertyCollateral collateral = null;
        if (entity.getPropertyValue() != null) {
            Money propertyValue = new Money(entity.getPropertyValue(), entity.getCurrency());
            collateral = new PropertyCollateral(
                    propertyValue,
                    Boolean.TRUE.equals(entity.getIsFirstHome()));
        }

        // Build Money objects
        Money requestedAmount = null;
        if (entity.getRequestedAmount() != null) {
            requestedAmount = new Money(entity.getRequestedAmount(), entity.getCurrency());
        }

        Money creditLimit = null;
        if (entity.getRequestedCreditLimit() != null) {
            creditLimit = new Money(entity.getRequestedCreditLimit(), entity.getCurrency());
        }

        // Build RequestDetails
        RequestDetails requestDetails = new RequestDetails(
                entity.getRequestType(),
                entity.getPurpose(),
                requestedAmount,
                entity.getRequestedTermMonths(),
                entity.getRequestedInterestRate(),
                creditLimit,
                Boolean.TRUE.equals(entity.getIsRevolving()),
                entity.getRepaymentSystem(),
                entity.getLoanType());

        // Convert LocalDate to Date
        Date creationDate = localDateToDate(entity.getRequestDate());
        Date lastReviewDate = localDateToDate(entity.getLastReviewDate());

        Request request = new Request(collateral, creationDate, requestDetails, entity.getStatus());
        request.setId(entity.getId().toHexString());
        request.setRequestCode(entity.getRequestCode());
        request.setPartyId(entity.getPartyId().toHexString());
        request.setLastReviewDate(lastReviewDate);
        return request;
    }

    /**
     * Helper method to convert LocalDate to Date.
     * 
     * @param localDate
     * @return the converted Date object, or null if localDate is null
     */
    private Date localDateToDate(LocalDate localDate) {
        if (localDate == null) {
            return null;
        }
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
}
