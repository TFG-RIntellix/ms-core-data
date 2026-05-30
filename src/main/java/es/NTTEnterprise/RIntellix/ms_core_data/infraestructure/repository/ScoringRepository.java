package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ScoringEntity;

/**
 * Repository interface for ScoringEntity in the "scorings" MongoDB collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-03-2026
 */
public interface ScoringRepository extends MongoRepository<ScoringEntity, ObjectId> {

    /**
     * Finds the most recent scoring for a given request ID, ordered by scoring_date
     * descending.
     * 
     * @param requestId The ID of the request (as stored in request_id field).
     * @return An Optional containing the latest ScoringEntity, or empty if none
     *         found.
     */
    @Query(value = "{ 'request_id': ?0 }")
    Optional<ScoringEntity> findByRequestId(ObjectId requestId);

}
