package es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.repository;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;

import es.NTTEnterprise.RIntellix.ms_core_data.infraestructure.entities.ReportEntity;

/**
 * Repository interface for ReportEntity in the "reports" MongoDB collection.
 * Extends Spring Data MongoDB's MongoRepository for standard CRUD operations.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-29-2026
 */
public interface ReportRepository extends MongoRepository<ReportEntity, ObjectId> {

}
