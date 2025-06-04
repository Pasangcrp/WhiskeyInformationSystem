package cqu.wis.roles;
import cqu.wis.data.WhiskeyData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WhiskeyDataManager {
    private WhiskeyData whiskeyData;
    private List<WhiskeyData.WhiskeyDetails> records;
    private int numberOfRecords;
    private int currentIndex;
    private WhiskeyData.WhiskeyDetails currentRecord;

    public WhiskeyDataManager(WhiskeyData whiskeyData) {
        this.whiskeyData = whiskeyData;
        this.records = new ArrayList<>();
        this.numberOfRecords = 0;
        this.currentIndex = -1;
        this.currentRecord = null;
    }

    public int findAllMalts() throws SQLException {
        records = whiskeyData.getAllMalts();
        numberOfRecords = records.size();
        System.out.println("Number of records in findAllMalts: " + numberOfRecords);
        currentIndex = (numberOfRecords == 0) ? -1 : 0;
        currentRecord = (numberOfRecords == 0) ? null : records.get(0);
        return numberOfRecords;
    }

    public WhiskeyData.WhiskeyDetails first() {
        if (numberOfRecords == 0) return null;
        currentIndex = 0;
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    public WhiskeyData.WhiskeyDetails next() {
        if (numberOfRecords == 0) return null;
        currentIndex = (currentIndex + 1) % numberOfRecords;
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    public WhiskeyData.WhiskeyDetails previous() {
        if (numberOfRecords == 0) return null;
        currentIndex = (currentIndex - 1 + numberOfRecords) % numberOfRecords;
        currentRecord = records.get(currentIndex);
        return currentRecord;
    }

    public void setDetails(WhiskeyData.WhiskeyDetails[] details) {
        List<WhiskeyData.WhiskeyDetails> list = Arrays.asList(details);
        records = new ArrayList<>(list);
        numberOfRecords = records.size();
        currentIndex = (numberOfRecords == 0) ? -1 : 0;
        currentRecord = (numberOfRecords == 0) ? null : records.get(0);
    }

    public int findMaltsByRegion(String region) throws SQLException {
        records = whiskeyData.getMaltsByRegion(region);
        numberOfRecords = records.size();
        System.out.println("Number of records in findMaltsByRegion: " + numberOfRecords);
        currentIndex = (numberOfRecords == 0) ? -1 : 0;
        currentRecord = (numberOfRecords == 0) ? null : records.get(0);
        return numberOfRecords;
    }

    public int findMaltsByAgeRange(int minAge, int maxAge) throws SQLException {
        records = whiskeyData.getMaltsByAgeRange(minAge, maxAge);
        numberOfRecords = records.size();
        System.out.println("Number of records in findMaltsByAgeRange: " + numberOfRecords);
        currentIndex = (numberOfRecords == 0) ? -1 : 0;
        currentRecord = (numberOfRecords == 0) ? null : records.get(0);
        return numberOfRecords;
    }
}