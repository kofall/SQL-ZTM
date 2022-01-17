/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import java.sql.*;
/**
 * FXML Controller class
 *
 * @author kofal
 */
public class sLoginFXController implements Initializable {

    @FXML
    private Label lb_Error;
    @FXML
    private TextField tf_Username;
    @FXML
    private TextField tf_Password;

    private Stage stage = null;
    private User user = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    @FXML
    private void exit(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            stage.close();
        }
    }

    @FXML
    private void login(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
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
                try (CallableStatement cstmt1 = conn.prepareCall("{? = call zalogujUzytkownika(?,?)}");
                     CallableStatement cstmt2 = conn.prepareCall("{? = call getTypUzytkownika(?)}");){

                    cstmt1.registerOutParameter(1,Types.INTEGER);
                    cstmt1.setString(2, tf_Username.getText());
                    cstmt1.setString(3, tf_Password.getText());
                    cstmt1.execute();
                    int success = cstmt1.getInt(1);
                    if(success == 1){
                        user = new User(tf_Username.getText(),tf_Password.getText());
                        cstmt2.registerOutParameter(1,Types.CHAR);
                        cstmt2.setString(2,tf_Username.getText());
                        cstmt2.execute();
                        String user_type = cstmt2.getString(1);
                        if(user_type.equals("A")){
                            swapScenes_admin();
                        }else if(user_type.equals("K")){
                            swapScenes_user();
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Login Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Unrecognized user type!");
                            alert.showAndWait();
                        }
                    }else if(success == 0){
                        lb_Error.setText("Błędne dane logowania!");
                        lb_Error.setVisible(true);
                    }
                }catch (SQLException ex) {
                    System.out.println(ex.getMessage());
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Login Error");
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
    }

    @FXML
    private void register(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "startup/registerFXML", "Zarejestruj się");
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    private void swapScenes_admin() {
        try {
            new Swapper(false, stage, user, null, null, "admin/adminFXML", null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }

    private void swapScenes_user() {
        try {
            new Swapper(false, stage, user, null, null, "user/userFXML", null);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
