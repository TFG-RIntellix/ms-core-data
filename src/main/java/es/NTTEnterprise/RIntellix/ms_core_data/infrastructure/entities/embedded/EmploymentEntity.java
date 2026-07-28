package es.NTTEnterprise.RIntellix.ms_core_data.infrastructure.entities.embedded;

import org.springframework.data.mongodb.core.mapping.Field;

/**
 * Embedded document for party employment information.
 * Contains employment status and work experience data.
 * 
 * @author Lucía Fernández Mancebo
 * @Date 03-01-2026
 */
public class EmploymentEntity {

    @Field("status")
    private String status;

    @Field("sector")
    private String sector;

    @Field("occupation")
    private String occupation;

    @Field("employer_seniority_years")
    private Integer employerSeniorityYears;

    @Field("total_work_experience_years")
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

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
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
