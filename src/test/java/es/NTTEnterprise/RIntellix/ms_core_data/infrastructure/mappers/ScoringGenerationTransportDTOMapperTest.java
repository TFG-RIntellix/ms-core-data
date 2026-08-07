package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input.ScoringGenerationDTO;
import es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.output.ScoringGenerationRequest;

class ScoringGenerationTransportDTOMapperTest {

    @Test
    @DisplayName("Should map null to null")
    void toTransportDTO_null() {
        assertNull(ScoringGenerationTransportDTOMapper.toTransportDTO(null));
    }

    @Test
    @DisplayName("Should map ScoringGenerationRequest to ScoringGenerationDTO successfully")
    void toTransportDTO_success() {
        ScoringGenerationRequest req = new ScoringGenerationRequest();
        req.setRequestId("REQ-1");
        req.setPartyId("P-1");
        req.setAge(40);
        req.setGender("MALE");
        req.setMaritalStatus("MARRIED");
        req.setEducation("UNIVERSITY");
        req.setDependents(2);
        req.setHomeOwnership("OWNER");
        req.setHasMortgage(true);
        req.setEmploymentStatus("INDEFINIDO");
        req.setOccupationSector("IT");
        req.setAnnualIncome(50000.0);
        req.setExistingObligations(500.0);
        req.setRequestType("PRESTAMO");
        req.setPurpose("REFORMA");
        req.setLoanType("Personal");
        req.setLoanAmount(15000.0);
        req.setTermMonths(36);
        req.setInterestRate(5.5);
        req.setLtv(0.8);
        req.setDti(0.3);
        req.setPreviousLoansCount(1);
        req.setPreviousDefaultsCount(0);

        ScoringGenerationDTO dto = ScoringGenerationTransportDTOMapper.toTransportDTO(req);

        assertEquals("REQ-1", dto.getRequestId());
        assertEquals("P-1", dto.getPartyId());
        assertEquals(40, dto.getAge());
        assertEquals("MALE", dto.getGender());
        assertEquals("MARRIED", dto.getMaritalStatus());
        assertEquals("UNIVERSITY", dto.getEducation());
        assertEquals(2, dto.getDependents());
        assertEquals("OWNER", dto.getHomeOwnership());
        assertTrue(dto.getHasMortgage());
        assertEquals("INDEFINIDO", dto.getEmploymentStatus());
        assertEquals("IT", dto.getOccupationSector());
        assertEquals(50000.0, dto.getAnnualIncome());
        assertEquals(500.0, dto.getExistingObligations());
        assertEquals("PRESTAMO", dto.getRequestType());
        assertEquals("REFORMA", dto.getPurpose());
        assertEquals("Personal", dto.getLoanType());
        assertEquals(15000.0, dto.getLoanAmount());
        assertEquals(36, dto.getTermMonths());
        assertEquals(5.5, dto.getInterestRate());
        assertEquals(0.8, dto.getLtv());
        assertEquals(0.3, dto.getDti());
        assertEquals(1, dto.getPreviousLoansCount());
        assertEquals(0, dto.getPreviousDefaultsCount());
    }
}
