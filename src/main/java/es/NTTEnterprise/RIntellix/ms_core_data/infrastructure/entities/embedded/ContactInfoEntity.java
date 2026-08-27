package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded document for party contact information.
 * Contains phone, email and address data.
 * NOTE: These fields are not in the original Party schema but are required
 * for the RequestDetailsDTO mapping.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class ContactInfoEntity {
    private String phoneNumber;
    private String email;
    private String address;

    public ContactInfoEntity() {
    }

    // Getters and Setters

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
