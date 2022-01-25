/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aKierowcaInfoFXController implements Initializable {

    @FXML
    private Label lb_Pesel;
    @FXML
    private Label lb_Name;
    @FXML
    private Label lb_Surname;

    private Stage stage = null;
    private User user = null;
    private String pesel;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object record) {
        pesel = (String) ((Map<String,Object>) record).get("kierowca");
        lb_Pesel.setText(pesel);
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM kierowca WHERE pesel = ?"); ){
                pstmt1.setString(1,pesel);
                ResultSet rs = pstmt1.executeQuery();
                while(rs.next()){

                    lb_Name.setText(rs.getString(2));
                    lb_Surname.setText(rs.getString(3));

                }
                rs.close();

            }catch (SQLException ex) {
                System.out.println(ex.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Loading Error");
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
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            stage.close();
        }
    }
}
