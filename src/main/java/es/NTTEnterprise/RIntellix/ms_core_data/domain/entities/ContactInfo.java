package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * Value object representing the contact information of a person.
 * Contains phone, email and address data.
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class ContactInfo {

    private String phoneNumber;
    private String email;
    private String address;

    public ContactInfo() {
    }

    public ContactInfo(String phoneNumber, String email, String address) {
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
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
