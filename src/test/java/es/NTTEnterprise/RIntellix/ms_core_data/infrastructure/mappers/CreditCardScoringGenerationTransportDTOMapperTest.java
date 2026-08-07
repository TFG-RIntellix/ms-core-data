package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.CreditCardScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

class CreditCardScoringGenerationTransportDTOMapperTest {

    @Test
    @DisplayName("Should map null to null")
    void toTransportDTO_null() {
        assertNull(CreditCardScoringGenerationTransportDTOMapper.toTransportDTO(null));
    }

    @Test
    @DisplayName("Should map ScoringGenerationRequest to CreditCardScoringGenerationDTO successfully")
    void toTransportDTO_success() {
        ScoringGenerationRequest req = new ScoringGenerationRequest();
        req.setRequestId("REQ-1");
        req.setPartyId("P-1");
        req.setRequestType("TARJETA_CREDITO");
        req.setAge(35);
        req.setGender("FEMALE");
        req.setMaritalStatus("SINGLE");
        req.setEmploymentStatus("INDEFINIDO");
        req.setEmploymentSeniorityYears(5.0);
        req.setAnnualIncome(40000.0);
        req.setIncomeType("Salario");
        req.setHomeOwnership("OWNER");
        req.setExistingObligations(200.0);
        req.setCreditLimit(3000.0);
        req.setIsRevolving(true);
        req.setInterestRate(18.0);
        req.setDependents(1);
        req.setLti(0.1);
        req.setDti(0.2);
        req.setPreviousDefaultsCount(0);

        CreditCardScoringGenerationDTO dto = CreditCardScoringGenerationTransportDTOMapper.toTransportDTO(req);

        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("P-1", dto.getPartyId());
        assertEquals("TARJETA_CREDITO", dto.getRequestType());
        assertEquals(35, dto.getAge());
        assertEquals("FEMALE", dto.getGender());
        assertEquals("SINGLE", dto.getMaritalStatus());
        assertEquals("INDEFINIDO", dto.getEmploymentStatus());
        assertEquals(5.0, dto.getEmploymentSeniorityYears());
        assertEquals(40000.0, dto.getAnnualIncome());
        assertEquals("Salario", dto.getIncomeType());
        assertEquals("OWNER", dto.getHomeOwnership());
        assertEquals(200.0, dto.getExistingObligations());
        assertEquals(3000.0, dto.getCreditLimit());
        assertTrue(dto.getIsRevolving());
        assertEquals(18.0, dto.getInterestRate());
        assertEquals(1, dto.getDependents());
        assertEquals(0.1, dto.getLti());
        assertEquals(0.2, dto.getDti());
        assertEquals(0, dto.getPreviousDefaultsCount());
    }
}
