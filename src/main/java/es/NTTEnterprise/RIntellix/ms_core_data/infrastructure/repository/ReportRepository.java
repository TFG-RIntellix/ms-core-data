package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.repository;

import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.ReportEntity;

/**
 * Repository interface for ReportEntity in the "reports" MongoDB collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @date 29/03/2026
 */
public interface ReportRepository extends MongoRepository<ReportEntity, ObjectId> {

    /**
     * Finds a single report by request ID.
     * 
     * @param requestId the ID to check against request_id
     * @return an Optional containing the matching report entity, or empty
     */
    @Query("{ 'request_id': ?0 }")
    Optional<ReportEntity> findByRequestId(ObjectId requestId);
}
