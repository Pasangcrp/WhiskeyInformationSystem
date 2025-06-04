package cqu.wis.roles;
import cqu.wis.data.WhiskeyData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class WhiskeyDataManagerTest {
    private WhiskeyDataManager wdm;

    @BeforeEach
    public void setUp() {
        wdm = new WhiskeyDataManager(null);
    }

    @Test
    public void nextWithNoRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null, wdm.next());
    }

    @Test
    public void nextWithOneRecordTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.next());
        assertEquals(details[0], wdm.next());
    }

    @Test
    public void nextWithTwoRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.first());
        assertEquals(details[1], wdm.next());
        assertEquals(details[0], wdm.next());
    }

    @Test
    public void previousWithNoRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {};
        wdm.setDetails(details);
        assertEquals(null, wdm.previous());
    }

    @Test
    public void previousWithOneRecordTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.previous());
        assertEquals(details[0], wdm.previous());
    }

    @Test
    public void previousWithTwoRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.first());
        assertEquals(details[1], wdm.previous());
        assertEquals(details[0], wdm.previous());
    }

    @Test
    public void nextWithThreeRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.first());
        assertEquals(details[1], wdm.next());
        assertEquals(details[2], wdm.next());
        assertEquals(details[0], wdm.next());
    }

    @Test
    public void previousWithThreeRecordsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00)
        };
        wdm.setDetails(details);
        assertEquals(details[0], wdm.first());
        assertEquals(details[2], wdm.previous());
        assertEquals(details[1], wdm.previous());
        assertEquals(details[0], wdm.previous());
    }

    @Test
    public void nextStartingFromMiddleTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00)
        };
        wdm.setDetails(details);
        wdm.first(); // Start at first record (Glenmorangie)
        wdm.next(); // Move to second record (Talisker)
        assertEquals(details[2], wdm.next()); // Move to third record (Laphroaig)
        assertEquals(details[0], wdm.next()); // Cycle back to first record (Glenmorangie)
        assertEquals(details[1], wdm.next()); // Move to second record (Talisker)
    }

    @Test
    public void previousStartingFromMiddleTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00)
        };
        wdm.setDetails(details);
        wdm.first(); // Start at first record (Glenmorangie)
        wdm.next(); // Move to second record (Talisker)
        assertEquals(details[0], wdm.previous()); // Move to first record (Glenmorangie)
        assertEquals(details[2], wdm.previous()); // Cycle back to third record (Laphroaig)
        assertEquals(details[1], wdm.previous()); // Move to second record (Talisker)
    }

    @Test
    public void multipleNextIterationsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00),
            new WhiskeyData.WhiskeyDetails(4, "Macallan", 15, "Speyside", 75.00)
        };
        wdm.setDetails(details);
        wdm.first();
        assertEquals(details[1], wdm.next());
        assertEquals(details[2], wdm.next());
        assertEquals(details[3], wdm.next());
        assertEquals(details[0], wdm.next());
        assertEquals(details[1], wdm.next());
        assertEquals(details[2], wdm.next());
    }

    @Test
    public void multiplePreviousIterationsTest() {
        WhiskeyData.WhiskeyDetails[] details = {
            new WhiskeyData.WhiskeyDetails(1, "Glenmorangie", 10, "Highland", 45.00),
            new WhiskeyData.WhiskeyDetails(2, "Talisker", 18, "Isle of Skye", 85.00),
            new WhiskeyData.WhiskeyDetails(3, "Laphroaig", 12, "Islay", 55.00),
            new WhiskeyData.WhiskeyDetails(4, "Macallan", 15, "Speyside", 75.00)
        };
        wdm.setDetails(details);
        wdm.first();
        assertEquals(details[3], wdm.previous());
        assertEquals(details[2], wdm.previous());
        assertEquals(details[1], wdm.previous());
        assertEquals(details[0], wdm.previous());
        assertEquals(details[3], wdm.previous());
        assertEquals(details[2], wdm.previous());
    }
}