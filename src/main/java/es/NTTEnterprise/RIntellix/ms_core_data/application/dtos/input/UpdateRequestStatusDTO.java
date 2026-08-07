package es.NTTEnterprise.RIntellix.ms_core_data.application.dtos.input;

import jakarta.validation.constraints.NotNull;

/**
 * Input DTO for the PUT endpoint that updates a request's status.
 * Contains the new status value as a string matching {@code RequestStatus} enum names.
 *
 * @author Lucía Fernández Mancebo
 * @date 05/08/2026
 */
public class UpdateRequestStatusDTO {

    @NotNull(message = "Request status must be provided")
    private String requestStatus;

    public UpdateRequestStatusDTO() {
    }

    public String getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        this.requestStatus = requestStatus;
    }
}
