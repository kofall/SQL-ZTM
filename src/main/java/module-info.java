module com.example.ztm {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens com.example.ztm to javafx.fxml;
    exports com.example.ztm;
}