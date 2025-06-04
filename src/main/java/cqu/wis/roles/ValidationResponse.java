package cqu.wis.roles;

/**
 * A record representing the result of a validation operation.
 * @param isValid True if validation passed, false otherwise
 * @param message A message explaining the validation result (e.g., error details)
 */
public record ValidationResponse(boolean isValid, String message) {
    /**
     * Compact constructor to validate the message parameter.
     * @throws IllegalArgumentException if the message is null
     */
    public ValidationResponse {
        if (message == null) {
            throw new IllegalArgumentException("Validation message cannot be null");
        }
    }
}