package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

/**
 * Embedded document for party employment information.
 * Contains employment status and work experience data.
 * 
 * @author Lucía Fernández Mancebo
 * @date 01/03/2026
 */
public class EmploymentEntity {
    private String status;
    private String occupationSector;
    private String occupation;
    private Integer employerSeniorityYears;
    private Integer totalWorkExperienceYears;

    public EmploymentEntity() {
    }

    // Getters and Setters

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOccupationSector() {
        return occupationSector;
    }

    public void setOccupationSector(String occupationSector) {
        this.occupationSector = occupationSector;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public Integer getEmployerSeniorityYears() {
        return employerSeniorityYears;
    }

    public void setEmployerSeniorityYears(Integer employerSeniorityYears) {
        this.employerSeniorityYears = employerSeniorityYears;
    }

    public Integer getTotalWorkExperienceYears() {
        return totalWorkExperienceYears;
    }

    public void setTotalWorkExperienceYears(Integer totalWorkExperienceYears) {
        this.totalWorkExperienceYears = totalWorkExperienceYears;
    }
}
