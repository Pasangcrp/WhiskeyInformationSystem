package cqu.wis.data;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WhiskeyData {
    private Connection connection;
    private PreparedStatement getMaltsByRegionStmt;
    private PreparedStatement getMaltsByAgeRangeStmt;
    private final String url;
    private final String username;
    private final String password;

    public WhiskeyData(String url, String username, String password) throws SQLException {
        this.url = url;
        this.username = username;
        this.password = password;
        this.connection = DriverManager.getConnection(url, username, password);
        // Initialize prepared statements for the new queries
        this.getMaltsByRegionStmt = connection.prepareStatement(
            "SELECT * FROM Whiskey WHERE region = ?"
        );
        this.getMaltsByAgeRangeStmt = connection.prepareStatement(
            "SELECT * FROM Whiskey WHERE age BETWEEN ? AND ?"
        );
    }

    /**
     * Connects to the database. Called by App.start() after instantiation.
     * If the connection is already established, this method verifies it.
     * @throws SQLException if the connection cannot be established
     */
    public void connect() throws SQLException {
        if (connection == null || connection.isClosed()) {
            this.connection = DriverManager.getConnection(url, username, password);
            // Re-initialize prepared statements if the connection was closed
            this.getMaltsByRegionStmt = connection.prepareStatement(
                "SELECT * FROM Whiskey WHERE region = ?"
            );
            this.getMaltsByAgeRangeStmt = connection.prepareStatement(
                "SELECT * FROM Whiskey WHERE age BETWEEN ? AND ?"
            );
        }
    }

    public static class WhiskeyDetails {
        private int id;
        private String distillery;
        private int age;
        private String region;
        private double price;

        public WhiskeyDetails(int id, String distillery, int age, String region, double price) {
            this.id = id;
            this.distillery = distillery;
            this.age = age;
            this.region = region;
            this.price = price;
        }

        public int getId() { return id; }
        public String getDistillery() { return distillery; }
        public int getAge() { return age; }
        public String getRegion() { return region; }
        public double getPrice() { return price; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            WhiskeyDetails that = (WhiskeyDetails) o;
            return id == that.id &&
                   age == that.age &&
                   Double.compare(that.price, price) == 0 &&
                   Objects.equals(distillery, that.distillery) &&
                   Objects.equals(region, that.region);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, distillery, age, region, price);
        }
    }

    public List<WhiskeyDetails> getAllMalts() throws SQLException {
        List<WhiskeyDetails> malts = new ArrayList<>();
        String query = "SELECT * FROM Whiskey";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            int count = 0;
            while (rs.next()) {
                int id = rs.getInt("id");
                String distillery = rs.getString("distillery");
                int age = rs.getInt("age");
                String region = rs.getString("region");
                double price = rs.getDouble("price");
                malts.add(new WhiskeyDetails(id, distillery, age, region, price));
                count++;
                System.out.println("Record " + count + ": " + distillery);
            }
            System.out.println("Total records retrieved in getAllMalts: " + count);
        }
        return malts;
    }

    public List<WhiskeyDetails> getMaltsByRegion(String region) throws SQLException {
        List<WhiskeyDetails> malts = new ArrayList<>();
        getMaltsByRegionStmt.setString(1, region);
        try (ResultSet rs = getMaltsByRegionStmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String distillery = rs.getString("distillery");
                int age = rs.getInt("age");
                String regionResult = rs.getString("region");
                double price = rs.getDouble("price");
                malts.add(new WhiskeyDetails(id, distillery, age, regionResult, price));
            }
        }
        return malts;
    }

    public List<WhiskeyDetails> getMaltsByAgeRange(int minAge, int maxAge) throws SQLException {
        List<WhiskeyDetails> malts = new ArrayList<>();
        getMaltsByAgeRangeStmt.setInt(1, minAge);
        getMaltsByAgeRangeStmt.setInt(2, maxAge);
        try (ResultSet rs = getMaltsByAgeRangeStmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String distillery = rs.getString("distillery");
                int age = rs.getInt("age");
                String region = rs.getString("region");
                double price = rs.getDouble("price");
                malts.add(new WhiskeyDetails(id, distillery, age, region, price));
            }
        }
        return malts;
    }
}