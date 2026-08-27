package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.adapters.output;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

import es.NTTEnterprise.RIntellix.ms_core_data.domain.entities.Party;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.exceptions.EntityNotFoundException;
import es.NTTEnterprise.RIntellix.ms_core_data.domain.ports.output.PartyPortRepository;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.PartyEntity;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.mappers.PartyMapper;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.projections.PartyNameProjection;
import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository.PartyRepository;
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
 * @date 01/03/2026
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

    @Override
    public Map<String, Party> findPartyNames(Set<String> partyIds) {
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_NAMES_ONLY_START, partyIds != null ? partyIds.size() : 0);
        
        if (partyIds == null || partyIds.isEmpty()) {
            return Collections.emptyMap();
        }

        List<ObjectId> objectIds = partyIds.stream()
                .map(ObjectId::new)
                .collect(Collectors.toList());

        List<PartyNameProjection> projections = partyRepository.findPartyNameProjectionsByIdIn(objectIds);

        return projections.stream()
                .collect(Collectors.toMap(
                        PartyNameProjection::getId,
                        partyMapper::toPartialDomain
                ));
    }

    @Override
    public Set<String> findPartyIdsByNameMatch(String searchTerm) {
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_IDS_BY_NAME_START, searchTerm);

        if (searchTerm == null || searchTerm.isBlank()) {
            return Collections.emptySet();
        }

        List<PartyNameProjection> projections = partyRepository.findPartyNameProjectionsByNameMatch(searchTerm);

        Set<String> partyIds = projections.stream()
                .map(PartyNameProjection::getId)
                .collect(Collectors.toSet());
        
        log.debug(LogMessage.REPOSITORY_PARTY_FIND_IDS_BY_NAME_RESULT, partyIds.size());
        
        return partyIds;
    }

}
