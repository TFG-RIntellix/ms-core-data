package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import jakarta.validation.constraints.NotNull;

/**
 * Input DTO for the PATCH endpoint that performs a soft delete on a simulation.
 * Only allows modifying the archived status of the simulation.
 *
 * @author Lucía Fernández Mancebo
 * @Date 03-07-2026
 */
public class ArchiveSimulationDTO {

    @NotNull(message = "Archived status must be provided")
    private Boolean isArchived;

    public ArchiveSimulationDTO() {
    }

    public Boolean getIsArchived() {
        return isArchived;
    }

    public void setIsArchived(Boolean isArchived) {
        this.isArchived = isArchived;
    }
}
