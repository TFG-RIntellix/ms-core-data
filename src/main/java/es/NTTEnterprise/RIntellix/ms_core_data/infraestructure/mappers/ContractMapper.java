package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.time.LocalDate;

import org.springframework.stereotype.Component;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.CreditCardContract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.LoanContract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Money;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.MortgageContract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractType;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.Purpose;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ContractEntity;

/**
 * Mapper class to convert between ContractEntity (infrastructure) and Contract domain hierarchy.
 * Uses a Map-based strategy pattern to dispatch mapping by contract type,
 * avoiding switch statements and ensuring easy extensibility when new contract types are added.
 *
 * To add a new contract type:
 *   1. Create a new domain subclass of Contract
 *   2. Add a private mapping method in this class
 *   3. Register it in the MAPPERS map
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
@Component
public class ContractMapper {

    /**
     * Strategy map: maps each contract_type string to the function that produces the correct domain subclass.
     * This replaces switches/if-else chains and scales linearly with new types.
     */
    private static final Map<String, Function<ContractEntity, Contract>> MAPPERS = Map.of(
        "PRESTAMO", ContractMapper::mapToLoanContract,
        "HIPOTECA", ContractMapper::mapToMortgageContract,
        "TARJETA_CREDITO", ContractMapper::mapToCreditCardContract
    );

    /**
     * Converts a ContractEntity to the appropriate Contract domain subclass.
     *
     * @param entity the infrastructure entity from MongoDB
     * @return the Contract domain object (LoanContract, MortgageContract or CreditCardContract)
     * @throws IllegalArgumentException if the contract type is unknown
     */
    public Contract toDomain(ContractEntity entity) {
        if (entity == null) {
            return null;
        }

        Function<ContractEntity, Contract> mapper = MAPPERS.get(entity.getContractType());
        if (mapper == null) {
            throw new IllegalArgumentException("Unknown contract type: " + entity.getContractType());
        }

        return mapper.apply(entity);
    }

    /**
     * Converts a list of ContractEntity objects to domain Contract objects.
     *
     * @param entities the list of infrastructure entities
     * @return list of domain contracts
     */
    public List<Contract> toDomainList(List<ContractEntity> entities) {
        if (entities == null || entities.isEmpty()) {
            return new ArrayList<>();
        }
        return entities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    // ============================================================
    // Private mapping methods for each contract type
    // ============================================================

    private static LoanContract mapToLoanContract(ContractEntity entity) {
        LoanContract loan = new LoanContract();
        mapCommonFields(loan, entity);
        loan.setPrincipalAmount(new Money(entity.getPrincipalAmount(), entity.getCurrency()));
        loan.setTermMonths(entity.getTermMonths());
        loan.setMonthlyPayment(new Money(entity.getMonthlyPayment(), entity.getCurrency()));
        loan.setOutstandingBalance(new Money(entity.getOutstandingBalance(), entity.getCurrency()));
        loan.setPurpose(mapPurpose(entity.getPurpose()));
        return loan;
    }

    private static MortgageContract mapToMortgageContract(ContractEntity entity) {
        MortgageContract mortgage = new MortgageContract();
        mapCommonFields(mortgage, entity);
        mortgage.setPrincipalAmount(new Money(entity.getPrincipalAmount(), entity.getCurrency()));
        mortgage.setTermMonths(entity.getTermMonths());
        mortgage.setMonthlyPayment(new Money(entity.getMonthlyPayment(), entity.getCurrency()));
        mortgage.setOutstandingBalance(new Money(entity.getOutstandingBalance(), entity.getCurrency()));
        mortgage.setPurpose(mapPurpose(entity.getPurpose()));
        mortgage.setPropertyValue(new Money(entity.getPropertyValue(), entity.getCurrency()));
        mortgage.setIsFirstHome(entity.getIsFirstHome());
        return mortgage;
    }

    private static CreditCardContract mapToCreditCardContract(ContractEntity entity) {
        CreditCardContract card = new CreditCardContract();
        mapCommonFields(card, entity);
        card.setCreditLimit(new Money(entity.getCreditLimit(), entity.getCurrency()));
        card.setCurrentBalance(new Money(entity.getCurrentBalance(), entity.getCurrency()));
        card.setIsRevolving(entity.getIsRevolving());
        return card;
    }

    // ============================================================
    // Common field mapping
    // ============================================================

    private static void mapCommonFields(Contract contract, ContractEntity entity) {
        contract.setId(entity.getId());
        contract.setContractType(mapContractType(entity.getContractType()));;
        contract.setStatus(mapContractStatus(entity.getStatus()));
        contract.setOpenDate(mapDate(entity.getOpenDate()));
        contract.setInterestRate(entity.getInterestRate());
    }

    // ============================================================
    // Enum and type conversion helpers
    // ============================================================

    private static ContractType mapContractType(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ContractType.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static ContractStatus mapContractStatus(String value) {
        if (value == null) {
            return null;
        }
        try {
            return ContractStatus.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static Purpose mapPurpose(String value) {
        if (value == null) {
            return null;
        }
        try {
            return Purpose.valueOf(value);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private static LocalDate mapDate(java.util.Date date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    }
}
