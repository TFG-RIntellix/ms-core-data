package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.RequestEntity;

/**
 * Interface that serves as a repository for the Request aggregate, allowing to perform CRUD operations on the database. 
 * It extends the MongoRepository interface, which provides basic methods for working with MongoDB, and it also defines custom queries to find requests by party name and status.
 * @author Lucía Fernández Mancebo
 * @Date 02-28-2026
 */
public interface RequestRepository extends MongoRepository<RequestEntity, String> {
    
    /**
     * Finds a request by the name of the party associated with it.
     * @param partyName the name of the party associated with the request
     * @return the request entity that matches the given party name
     */
    @Query("{ 'party.name': ?0 }")
    List<RequestEntity> findByPartyName(String partyName);

    /**
     * Finds a request by its status.
     * @param status the status of the request (e.g., "Pendiente de Revision", "Revisado", "Aprobado", "Rechazado")
     * @return the request entity that matches the given status.
     */
    @Query("{ 'status': ?0 }")   
    List<RequestEntity> findByStatus(String status);
}
