package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ReportEntity;

/**
 * Repository interface for ReportEntity in the "reports" MongoDB collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
public interface ReportRepository extends MongoRepository<ReportEntity, ObjectId> {

    /**
     * Finds reports with dynamic filtering based on request ID and scoring ID.
     * Only non-null parameters are applied as filters.
     * 
     * @param requestId the ID of the associated request (optional filter)
     * @param scoringId the ID of the associated scoring (optional filter)
     * @return list of report entities matching the provided filters
     */
    @Query("{ $and: [ " +
            "{ $or: [ { $expr: { $eq: [:#{#requestId}, null] } }, { 'request_id': :#{#requestId} } ] }, " +
            "{ $or: [ { $expr: { $eq: [:#{#scoringId}, null] } }, { 'scoring_id': :#{#scoringId} } ] } " +
            "] }")
    List<ReportEntity> findWithFilters(@Param("requestId") ObjectId requestId, @Param("scoringId") ObjectId scoringId);

    /**
     * Finds reports that match either the request ID or the scoring ID.
     * Useful for generic searches where the user inputs a single ID.
     * 
     * @param requestId the ID to check against request_id
     * @param scoringId the ID to check against scoring_id
     * @return list of matching report entities
     */
    @Query("{ $or: [ { 'request_id': ?0 }, { 'scoring_id': ?1 } ] }")
    List<ReportEntity> findByRequestIdOrScoringId(ObjectId requestId, ObjectId scoringId);
}
