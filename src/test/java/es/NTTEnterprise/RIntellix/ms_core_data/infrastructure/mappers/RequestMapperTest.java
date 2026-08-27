package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import org.bson.types.ObjectId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Request;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.RequestType;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.RequestEntity;

class RequestMapperTest {

    private final RequestMapper mapper = new RequestMapper();

    @Test
    @DisplayName("Should map null to null")
    void toDomain_null() {
        assertNull(mapper.toDomain(null));
    }

    @Test
    @DisplayName("Should map RequestEntity to Request domain successfully")
    void toDomain_success() {
        RequestEntity entity = new RequestEntity();
        ObjectId id = new ObjectId();
        ObjectId partyId = new ObjectId();
        entity.setId(id);
        entity.setPartyId(partyId);
        entity.setRequestCode("REQ-2026-00001");
        entity.setRequestType(RequestType.PRESTAMO);
        entity.setPurpose(Purpose.COMPRA_VEHICULO);
        entity.setCurrency("EUR");
        entity.setLoanAmount(25000.0);
        entity.setTermMonths(48);
        entity.setInterestRate(4.5);
        entity.setStatus(RequestStatus.PENDIENTE_DE_REVISION);
        
        LocalDate reqDate = LocalDate.of(2026, 1, 1);
        entity.setRequestDate(reqDate);

        Request request = mapper.toDomain(entity);

        assertEquals(id.toHexString(), request.getId());
        assertEquals(partyId.toHexString(), request.getPartyId());
        assertEquals("REQ-2026-00001", request.getRequestCode());
        assertEquals(RequestType.PRESTAMO, request.getRequestDetails().getRequestType());
        assertEquals(Purpose.COMPRA_VEHICULO, request.getRequestDetails().getPurpose());
        assertEquals(25000.0, request.getRequestDetails().getRequestedAmount().getAmount());
        assertEquals("EUR", request.getRequestDetails().getRequestedAmount().getCurrency());
        assertEquals(48, request.getRequestDetails().getTermMonths());
        assertEquals(4.5, request.getRequestDetails().getInterestRate());
        assertEquals(RequestStatus.PENDIENTE_DE_REVISION, request.getRequestStatus());
        
        Date expectedDate = Date.from(reqDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        assertEquals(expectedDate, request.getCreationDate());
        assertNull(request.getCollateral());
    }

    @Test
    @DisplayName("Should map RequestEntity with mortgage specific fields")
    void toDomain_mortgage() {
        RequestEntity entity = new RequestEntity();
        entity.setId(new ObjectId());
        entity.setPartyId(new ObjectId());
        entity.setRequestType(RequestType.HIPOTECA);
        entity.setCurrency("EUR");
        entity.setPropertyValue(200000.0);
        entity.setIsFirstHome(true);

        Request request = mapper.toDomain(entity);

        assertNotNull(request.getCollateral());
        assertEquals(200000.0, request.getCollateral().getPropertyValue().getAmount());
        assertTrue(request.getCollateral().isFirstHome());
    }
}
