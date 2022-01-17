module com.example.ztm {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.ztm to javafx.fxml;
    exports com.example.ztm;
}