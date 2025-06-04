module cqu.wis {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    opens cqu.wis to javafx.fxml;
    exports cqu.wis;
    opens cqu.wis.view to javafx.fxml;
    exports cqu.wis.view;
    exports cqu.wis.roles; 
    exports cqu.wis.data;  
}