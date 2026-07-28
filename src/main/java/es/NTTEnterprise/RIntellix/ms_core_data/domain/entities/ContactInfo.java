package es.NTTEnterprise.RIntellix.ms_core_data.domain.entities;

/**
 * Value object representing the contact information of a person.
 * Contains phone, email and address data.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class ContactInfo {

    private String phoneNumber;
    private String email;
    private String address;

    /**
     * Default constructor for ContactInfo.
     */
    public ContactInfo() {
    }

    /**
     * Constructor for ContactInfo with all fields.
     * 
     * @param phoneNumber The phone number of the contact.
     * @param email       The email address of the contact.
     * @param address     The mailing address of the contact.
     */
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

    // toString, hashCode and equals methods
    @Override
    public String toString() {
        return "ContactInfo [phoneNumber=" + phoneNumber + ", email=" + email + ", address=" + address + "]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
        result = prime * result + ((email == null) ? 0 : email.hashCode());
        result = prime * result + ((address == null) ? 0 : address.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        ContactInfo other = (ContactInfo) obj;
        if (phoneNumber == null) {
            if (other.phoneNumber != null)
                return false;
        } else if (!phoneNumber.equals(other.phoneNumber))
            return false;
        if (email == null) {
            if (other.email != null)
                return false;
        } else if (!email.equals(other.email))
            return false;
        if (address == null) {
            if (other.address != null)
                return false;
        } else if (!address.equals(other.address))
            return false;
        return true;
    }

}
