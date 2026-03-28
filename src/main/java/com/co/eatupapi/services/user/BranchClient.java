package com.co.eatupapi.services.user;

import com.co.eatupapi.utils.user.exceptions.UserValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

/**
 * Client to validate branch existence via an external HTTP endpoint.
 *
 * <p><b>Limitation:</b> There is no known branch/sede endpoint in the current project.
 * This client is prepared to call a configurable URL. If the URL is not configured
 * or the external service is unreachable, branch validation is skipped with a warning log.
 * This is a transitional design - once a branch service exists, configure the URL
 * via the environment variable BRANCH_SERVICE_URL.</p>
 */
@Component
public class BranchClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(BranchClient.class);

    private final RestClient restClient;
    private final String branchServiceUrl;

    public BranchClient(@Value("${branch.service.url:}") String branchServiceUrl) {
        this.branchServiceUrl = branchServiceUrl;
        this.restClient = RestClient.create();
    }

    /**
     * Validates that the given branchId exists.
     * If the branch service URL is not configured, validation is skipped.
     *
     * @param branchId the branch UUID to validate
     * @throws UserValidationException if the branch does not exist
     */
    public void validateBranchExists(UUID branchId) {
        if (branchId == null) {
            throw new UserValidationException("Field 'branchId' is required");
        }

        if (branchServiceUrl == null || branchServiceUrl.isBlank()) {
            return;
        }

        try {
            restClient.get()
                    .uri(branchServiceUrl + "/" + branchId)
                    .retrieve()
                    .toBodilessEntity();
        } catch (Exception ex) {
            LOGGER.warn("Branch validation failed for branchId {}", branchId, ex);
            throw new UserValidationException("Selected branch could not be validated");
        }
    }

    /**
     * Resolves the branch name for display in responses.
     * Returns the UUID string if the branch service is not configured.
     */
    public String getBranchName(UUID branchId) {
        if (branchServiceUrl == null || branchServiceUrl.isBlank()) {
            return branchId != null ? branchId.toString() : null;
        }

        try {
            return branchId != null ? branchId.toString() : null;
        } catch (Exception ex) {
            return branchId != null ? branchId.toString() : null;
        }
    }
}
