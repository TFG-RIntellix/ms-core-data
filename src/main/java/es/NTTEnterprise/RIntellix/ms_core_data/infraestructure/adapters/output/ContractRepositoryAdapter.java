package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Contract;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.enums.ContractStatus;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.ContractPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ContractEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.ContractMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.ContractRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;

import lombok.extern.slf4j.Slf4j;

/**
 * Adapter that implements ContractPortRepository, bridging the domain layer
 * with the infrastructure layer for contract persistence.
 * Converts ContractEntity objects from MongoDB into domain Contract objects
 * using the ContractMapper.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
@Slf4j
@Repository
public class ContractRepositoryAdapter implements ContractPortRepository {

    private final ContractRepository contractRepository;
    private final ContractMapper contractMapper;

    public ContractRepositoryAdapter(ContractRepository contractRepository, ContractMapper contractMapper) {
        this.contractRepository = contractRepository;
        this.contractMapper = contractMapper;
    }

    @Override
    public List<Contract> findByPartyId(String partyId) {
        log.debug(LogMessage.REPOSITORY_CONTRACT_FIND_BY_PARTY_START, partyId);

        List<ContractEntity> entities = contractRepository.findByPartyId(new ObjectId(partyId));
        log.debug(LogMessage.REPOSITORY_CONTRACT_FIND_BY_PARTY_RESULT, partyId, entities.size());

        return contractMapper.toDomainList(entities);
    }

    @Override
    public List<Contract> findActiveByPartyId(String partyId) {
        log.debug(LogMessage.REPOSITORY_CONTRACT_FIND_ACTIVE_START, partyId);

        List<ContractEntity> entities;
        entities = contractRepository.findByPartyIdAndStatus(new ObjectId(partyId), ContractStatus.ACTIVO.name());
        log.debug(LogMessage.REPOSITORY_CONTRACT_FIND_ACTIVE_RESULT, partyId, entities.size());

        return contractMapper.toDomainList(entities);
    }
}
