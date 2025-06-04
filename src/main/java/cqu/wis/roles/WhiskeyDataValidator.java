package cqu.wis.roles;

/**
 * Validates input data for whiskey queries.
 */
public class WhiskeyDataValidator {

    /**
     * Validates an age range for whiskey queries.
     * @param minAge The minimum age as a string
     * @param maxAge The maximum age as a string
     * @return ValidationResponse indicating if the age range is valid and any error message
     */
    public ValidationResponse checkAgeRange(String minAge, String maxAge) {
        // Check if inputs are null or empty
        if (minAge == null || minAge.trim().isEmpty()) {
            return new ValidationResponse(false, "Minimum age cannot be empty");
        }
        if (maxAge == null || maxAge.trim().isEmpty()) {
            return new ValidationResponse(false, "Maximum age cannot be empty");
        }

        // Parse the ages
        int min;
        int max;
        try {
            min = Integer.parseInt(minAge.trim());
            max = Integer.parseInt(maxAge.trim());
        } catch (NumberFormatException e) {
            return new ValidationResponse(false, "Age must be a valid number");
        }

        // Validate age values
        if (min <= 0) {
            return new ValidationResponse(false, "Minimum age must be greater than 0");
        }
        if (max <= 0) {
            return new ValidationResponse(false, "Maximum age must be greater than 0");
        }
        if (min > max) {
            return new ValidationResponse(false, "Minimum age cannot be greater than maximum age");
        }

        return new ValidationResponse(true, "");
    }

    /**
     * Validates a region for whiskey queries.
     * @param region The region as a string
     * @return ValidationResponse indicating if the region is valid and any error message
     */
    public ValidationResponse checkRegion(String region) {
        // Check if region is null or empty
        if (region == null || region.trim().isEmpty()) {
            return new ValidationResponse(false, "Region cannot be empty");
        }

        // Check if region contains only letters and spaces
        String trimmedRegion = region.trim();
        if (!trimmedRegion.matches("[a-zA-Z\\s]+")) {
            return new ValidationResponse(false, "Region can only contain letters and spaces");
        }

        return new ValidationResponse(true, "");
    }
}