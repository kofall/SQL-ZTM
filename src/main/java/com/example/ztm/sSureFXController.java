/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class sSureFXController implements Initializable {
    @FXML
    private Label l_message;

    Map<String,Object> recordV;
    private Stage stage = null;
    private User user = null;
    private Object prevController = null;
    private String keyV;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object controller, Map<String,Object> record, String key) {
        prevController = controller;
        recordV = record;
        keyV = key;
        l_message.setText("Czy na pewno chcesz usunąć rekord o kluczu głównym: "+((String) record.get(key))+"?");
    }

    private void refreshTables() {
        try {
            prevController.getClass().getMethod("initTables").invoke(prevController);
        } catch (IllegalAccessException e) {
            "".isEmpty();
        } catch (InvocationTargetException e) {
            "".isEmpty();
        } catch (NoSuchMethodException e) {
            "".isEmpty();
        }
    }

    @FXML
    private void deleteANDclose(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            Connection conn = null;
            String connectionString =
                    "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/"+
                            "dblab02_students.cs.put.poznan.pl";
            Properties connectionProps = new Properties();
            connectionProps.put("user", "inf145326");
            connectionProps.put("password", "inf145326");
            try {
                conn = DriverManager.getConnection(connectionString,
                        connectionProps);
                try (CallableStatement cstmt1 = conn.prepareCall("{? = call usunKierowce(?)}");){

                    cstmt1.registerOutParameter(1, Types.INTEGER);
                    if (recordV.get(keyV) instanceof String){
                        cstmt1.setString(2,(String)recordV.get(keyV));
                    }else if (recordV.get(keyV) instanceof Integer){
                        cstmt1.setInt(2, (Integer)recordV.get(keyV));
                    }
                    cstmt1.execute();
                    int success = cstmt1.getInt(1);
                    if(success == 0){
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nie istnieje kierowca o takim PESELU!");
                        alert.showAndWait();
                    }else if(success == 1){

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Insert Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Udane usunięcie kierowcy!");
                        alert.showAndWait();
                    }
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Insert Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to execute query!");
                    alert.showAndWait();

                }
                try {
                    conn.close();
                } catch (SQLException ex) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Connection Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Failed to disconnect from the database!");
                    alert.showAndWait();
                }
            } catch (SQLException ex) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Connection Error");
                alert.setHeaderText(null);
                alert.setContentText("Failed to connect to the database!");
                alert.showAndWait();
            }
            refreshTables();
            close(event);
        }
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            refreshTables();
            stage.close();
        }
    }
}
