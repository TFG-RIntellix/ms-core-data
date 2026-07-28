package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.projections;

/**
 * Projection interface for retrieving only party name fields from MongoDB.
 * Used for efficient queries when only firstName and lastName are needed.
 * 
 * This is a Spring Data projection that maps to the demographics embedded
 * document
 * in PartyEntity, retrieving only the necessary fields to build a partial
 * Party.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-02-2026
 */
public interface PartyNameProjection {

    /**
     * Gets the party ID.
     * 
     * @return the party ID
     */
    String getId();

    /**
     * Gets the demographics projection containing name fields.
     * 
     * @return the demographics projection, or null if not present
     */
    DemographicsNameProjection getDemographics();

    /**
     * Nested projection interface for demographics name fields only.
     */
    interface DemographicsNameProjection {

        /**
         * Gets the first name of the party.
         * 
         * @return the first name
         */
        String getFirstName();

        /**
         * Gets the last name of the party.
         * 
         * @return the last name
         */
        String getLastName();
    }
}
