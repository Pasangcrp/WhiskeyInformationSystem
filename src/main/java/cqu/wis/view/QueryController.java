package cqu.wis.view;
import cqu.wis.data.WhiskeyData;
import cqu.wis.roles.WhiskeyDataManager;
import cqu.wis.roles.WhiskeyDataValidator;
import cqu.wis.roles.ValidationResponse;
import cqu.wis.roles.SceneCoordinator; 
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.Button;
import java.sql.SQLException;

/**
 * Controller for the query scene GUI in the Whiskey Information System.
 */
@SuppressWarnings("unused")
public class QueryController {
    @FXML private TextField distilleryField, ageField, regionField, priceField;
    @FXML private TextField regionInput, ageRangeLeft, ageRangeRight;
    @FXML private TextArea messageArea;
    @FXML private Button allMaltsButton, regionButton, ageRangeButton, prevButton, nextButton, clearButton, exitButton;

    private WhiskeyDataManager whiskeyDataManager;
    private WhiskeyDataValidator whiskeyDataValidator;
    private SceneCoordinator sceneCoordinator; // Added instance variable

    @FXML
    private void initialize() {
        // Removed previous code; now empty as dependencies are injected
    }

    /**
     * Injects dependencies into the controller.
     * @param sceneCoordinator The SceneCoordinator for scene management
     * @param whiskeyDataManager The WhiskeyDataManager for data access
     * @param whiskeyDataValidator The WhiskeyDataValidator for input validation
     */
    public void inject(SceneCoordinator sceneCoordinator, WhiskeyDataManager whiskeyDataManager, WhiskeyDataValidator whiskeyDataValidator) {
        this.sceneCoordinator = sceneCoordinator;
        this.whiskeyDataManager = whiskeyDataManager;
        this.whiskeyDataValidator = whiskeyDataValidator;
    }

    /**
     * Exits the application.
     */
    @FXML
    private void handleExit() {
        System.exit(0);
    }

    /**
     * Clears all text fields and the message area.
     */
    @FXML
    private void handleClear() {
        distilleryField.clear();
        ageField.clear();
        regionField.clear();
        priceField.clear();
        regionInput.clear();
        ageRangeLeft.clear();
        ageRangeRight.clear();
        messageArea.clear();
    }

    /**
     * Displays all malts from the database.
     */
    @FXML
    private void handleAllMalts() {
        try {
            int numRecords = whiskeyDataManager.findAllMalts();
            System.out.println("Number of records retrieved in handleAllMalts: " + numRecords);
            messageArea.setText("Records found: " + numRecords);
            WhiskeyData.WhiskeyDetails firstRecord = whiskeyDataManager.first();
            System.out.println("First record: " + (firstRecord != null ? firstRecord.getDistillery() : "null"));
            if (firstRecord != null) {
                distilleryField.setText(firstRecord.getDistillery());
                ageField.setText(String.valueOf(firstRecord.getAge()));
                regionField.setText(firstRecord.getRegion());
                priceField.setText(String.valueOf(firstRecord.getPrice()));
            } else {
                distilleryField.clear();
                ageField.clear();
                regionField.clear();
                priceField.clear();
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving malts: " + e.getMessage());
            messageArea.setText("Error retrieving malts: " + e.getMessage());
        }
    }

    /**
     * Displays the next record in the list.
     */
    @FXML
    private void handleNext() {
        WhiskeyData.WhiskeyDetails record = whiskeyDataManager.next();
        if (record != null) {
            distilleryField.setText(record.getDistillery());
            ageField.setText(String.valueOf(record.getAge()));
            regionField.setText(record.getRegion());
            priceField.setText(String.valueOf(record.getPrice()));
        }
    }

    /**
     * Displays the previous record in the list.
     */
    @FXML
    private void handlePrevious() {
        WhiskeyData.WhiskeyDetails record = whiskeyDataManager.previous();
        if (record != null) {
            distilleryField.setText(record.getDistillery());
            ageField.setText(String.valueOf(record.getAge()));
            regionField.setText(record.getRegion());
            priceField.setText(String.valueOf(record.getPrice()));
        }
    }

    /**
     * Queries and displays malts from the specified region.
     */
    @FXML
    private void handleRegion() {
        String region = regionInput.getText();
        ValidationResponse response = whiskeyDataValidator.checkRegion(region);
        if (!response.isValid()) {
            messageArea.setText(response.message());
            distilleryField.clear();
            ageField.clear();
            regionField.clear();
            priceField.clear();
            return;
        }

        try {
            int numRecords = whiskeyDataManager.findMaltsByRegion(region);
            messageArea.setText("Records found: " + numRecords);
            WhiskeyData.WhiskeyDetails firstRecord = whiskeyDataManager.first();
            if (firstRecord != null) {
                distilleryField.setText(firstRecord.getDistillery());
                ageField.setText(String.valueOf(firstRecord.getAge()));
                regionField.setText(firstRecord.getRegion());
                priceField.setText(String.valueOf(firstRecord.getPrice()));
            } else {
                distilleryField.clear();
                ageField.clear();
                regionField.clear();
                priceField.clear();
            }
        } catch (SQLException e) {
            messageArea.setText("Error retrieving malts by region: " + e.getMessage());
            distilleryField.clear();
            ageField.clear();
            regionField.clear();
            priceField.clear();
        }
    }

    /**
     * Queries and displays malts within the specified age range.
     */
    @FXML
    private void handleAgeRange() {
        String minAgeStr = ageRangeLeft.getText();
        String maxAgeStr = ageRangeRight.getText();
        ValidationResponse response = whiskeyDataValidator.checkAgeRange(minAgeStr, maxAgeStr);
        if (!response.isValid()) {
            messageArea.setText(response.message());
            distilleryField.clear();
            ageField.clear();
            regionField.clear();
            priceField.clear();
            return;
        }

        try {
            int minAge = Integer.parseInt(minAgeStr.trim());
            int maxAge = Integer.parseInt(maxAgeStr.trim());
            int numRecords = whiskeyDataManager.findMaltsByAgeRange(minAge, maxAge);
            messageArea.setText("Records found: " + numRecords);
            WhiskeyData.WhiskeyDetails firstRecord = whiskeyDataManager.first();
            if (firstRecord != null) {
                distilleryField.setText(firstRecord.getDistillery());
                ageField.setText(String.valueOf(firstRecord.getAge()));
                regionField.setText(firstRecord.getRegion());
                priceField.setText(String.valueOf(firstRecord.getPrice()));
            } else {
                distilleryField.clear();
                ageField.clear();
                regionField.clear();
                priceField.clear();
            }
        } catch (SQLException e) {
            messageArea.setText("Error retrieving malts by age range: " + e.getMessage());
            distilleryField.clear();
            ageField.clear();
            regionField.clear();
            priceField.clear();
        }
    }
}