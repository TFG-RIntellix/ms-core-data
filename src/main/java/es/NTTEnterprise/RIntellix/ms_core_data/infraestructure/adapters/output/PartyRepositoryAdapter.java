package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.adapters.output;

import java.util.Objects;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.mappers.PartyMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.projections.PartyNameProjection;
import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository.PartyRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.utils.LogMessage;
import lombok.extern.slf4j.Slf4j;

/**
 * Adapter class that implements the PartyPortRepository interface, providing
 * the implementation for retrieving Party aggregates from the MongoDB database
 * using the PartyRepository.
 * This class acts as a bridge between the domain layer and the infrastructure
 * layer, converting PartyEntity objects from the database into Party domain
 * objects using the PartyMapper.
 * Since this microservice is read-only, only retrieval methods are implemented
 * in this adapter.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
@Slf4j
@Repository
public class PartyRepositoryAdapter implements PartyPortRepository {

    private final PartyRepository partyRepository;
    private final PartyMapper partyMapper;

    public PartyRepositoryAdapter(PartyRepository partyRepository, PartyMapper partyMapper) {
        this.partyRepository = Objects.requireNonNull(partyRepository);
        this.partyMapper = Objects.requireNonNull(partyMapper);
    }

    @Override
    public Party findById(String partyId) throws EntityNotFoundException, IllegalArgumentException {
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_BY_ID_START, partyId);

        Optional<PartyEntity> partyEntityOpt = partyRepository.findById(new ObjectId(partyId));

        if (partyEntityOpt.isEmpty()) {
            log.warn(LogMessage.REPOSITORY_PARTY_FIND_BY_ID_NOT_FOUND, partyId);
            throw new EntityNotFoundException(String.format(LogMessage.EXCEPTION_PARTY_NOT_FOUND, partyId));
        }

        log.debug(LogMessage.REPOSITORY_PARTY_FIND_BY_ID_FOUND, partyId);
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_BY_ID_MAPPING, partyId);
        Party party = partyMapper.toDomain(partyEntityOpt.get());

        return party;

    }

    @Override
    public Party findPartyName(String partyId) {
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_NAME_ONLY_START, partyId);

        PartyNameProjection projection = partyRepository.findPartyNameProjectionById(new ObjectId(partyId));

        if (projection == null) {
            log.debug(LogMessage.REPOSITORY_PARTY_FIND_NAME_ONLY_NOT_FOUND, partyId);
            return null;
        }

        Party party = partyMapper.toPartialDomain(projection);
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_NAME_ONLY_RESULT, partyId,
                party.getPersonDetails() != null ? party.getPersonDetails().getFullName() : "null");

        return party;
    }

}
