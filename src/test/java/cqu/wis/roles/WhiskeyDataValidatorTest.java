package cqu.wis.roles;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhiskeyDataValidatorTest {
    private WhiskeyDataValidator validator;

    @BeforeEach
    public void setUp() {
        validator = new WhiskeyDataValidator();
    }

    // Tests for checkAgeRange()
    @Test
    public void checkAgeRangeWithNullMinAgeTest() {
        ValidationResponse response = validator.checkAgeRange(null, "20");
        assertFalse(response.isValid());
        assertEquals("Minimum age cannot be empty", response.message());
    }

    @Test
    public void checkAgeRangeWithEmptyMinAgeTest() {
        ValidationResponse response = validator.checkAgeRange("", "20");
        assertFalse(response.isValid());
        assertEquals("Minimum age cannot be empty", response.message());
    }

    @Test
    public void checkAgeRangeWithNullMaxAgeTest() {
        ValidationResponse response = validator.checkAgeRange("10", null);
        assertFalse(response.isValid());
        assertEquals("Maximum age cannot be empty", response.message());
    }

    @Test
    public void checkAgeRangeWithEmptyMaxAgeTest() {
        ValidationResponse response = validator.checkAgeRange("10", "  ");
        assertFalse(response.isValid());
        assertEquals("Maximum age cannot be empty", response.message());
    }

    @Test
    public void checkAgeRangeWithInvalidMinAgeTest() {
        ValidationResponse response = validator.checkAgeRange("abc", "20");
        assertFalse(response.isValid());
        assertEquals("Age must be a valid number", response.message());
    }

    @Test
    public void checkAgeRangeWithInvalidMaxAgeTest() {
        ValidationResponse response = validator.checkAgeRange("10", "xyz");
        assertFalse(response.isValid());
        assertEquals("Age must be a valid number", response.message());
    }

    @Test
    public void checkAgeRangeWithNegativeMinAgeTest() {
        ValidationResponse response = validator.checkAgeRange("-5", "20");
        assertFalse(response.isValid());
        assertEquals("Minimum age must be greater than 0", response.message());
    }

    @Test
    public void checkAgeRangeWithZeroMinAgeTest() {
        ValidationResponse response = validator.checkAgeRange("0", "20");
        assertFalse(response.isValid());
        assertEquals("Minimum age must be greater than 0", response.message());
    }

    @Test
    public void checkAgeRangeWithNegativeMaxAgeTest() {
        ValidationResponse response = validator.checkAgeRange("10", "-20");
        assertFalse(response.isValid());
        assertEquals("Maximum age must be greater than 0", response.message());
    }

    @Test
    public void checkAgeRangeWithZeroMaxAgeTest() {
        ValidationResponse response = validator.checkAgeRange("10", "0");
        assertFalse(response.isValid());
        assertEquals("Maximum age must be greater than 0", response.message());
    }

    @Test
    public void checkAgeRangeWithMinGreaterThanMaxTest() {
        ValidationResponse response = validator.checkAgeRange("20", "10");
        assertFalse(response.isValid());
        assertEquals("Minimum age cannot be greater than maximum age", response.message());
    }

    @Test
    public void checkAgeRangeWithValidRangeTest() {
        ValidationResponse response = validator.checkAgeRange("10", "20");
        assertTrue(response.isValid());
        assertEquals("", response.message());
    }

    @Test
    public void checkAgeRangeWithEqualMinMaxTest() {
        ValidationResponse response = validator.checkAgeRange("15", "15");
        assertTrue(response.isValid());
        assertEquals("", response.message());
    }

    // Tests for checkRegion()
    @Test
    public void checkRegionWithNullRegionTest() {
        ValidationResponse response = validator.checkRegion(null);
        assertFalse(response.isValid());
        assertEquals("Region cannot be empty", response.message());
    }

    @Test
    public void checkRegionWithEmptyRegionTest() {
        ValidationResponse response = validator.checkRegion("");
        assertFalse(response.isValid());
        assertEquals("Region cannot be empty", response.message());
    }

    @Test
    public void checkRegionWithWhitespaceRegionTest() {
        ValidationResponse response = validator.checkRegion("   ");
        assertFalse(response.isValid());
        assertEquals("Region cannot be empty", response.message());
    }

    @Test
    public void checkRegionWithInvalidCharactersTest() {
        ValidationResponse response = validator.checkRegion("Highland123");
        assertFalse(response.isValid());
        assertEquals("Region can only contain letters and spaces", response.message());
    }

    @Test
    public void checkRegionWithSpecialCharactersTest() {
        ValidationResponse response = validator.checkRegion("Highland@");
        assertFalse(response.isValid());
        assertEquals("Region can only contain letters and spaces", response.message());
    }

    @Test
    public void checkRegionWithValidRegionTest() {
        ValidationResponse response = validator.checkRegion("Highland");
        assertTrue(response.isValid());
        assertEquals("", response.message());
    }

    @Test
    public void checkRegionWithValidRegionWithSpacesTest() {
        ValidationResponse response = validator.checkRegion("Isle of Skye");
        assertTrue(response.isValid());
        assertEquals("", response.message());
    }
}