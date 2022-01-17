/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.Stack;

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
public class sRegisterFXController implements Initializable {

    @FXML
    private Label lb_Error;
    @FXML
    private TextField tf_Username;
    @FXML
    private TextField tf_Password;
    @FXML
    private TextField tf_Email;
    @FXML
    private TextField tf_Name;
    @FXML
    private TextField tf_Surname;

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
    private void register(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            lb_Error.setVisible(false);
            if(tf_Username.getText().equals("") || tf_Password.getText().equals("") || tf_Email.getText().equals("")
                    || tf_Name.getText().equals("") || tf_Surname.getText().equals("")){
                lb_Error.setText("Brakujące dane!");
                lb_Error.setVisible(true);
            }else{
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call zarejestrujUzytkownika(?,?,?,?,?)}");){
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2, tf_Username.getText());
                        cstmt1.setString(3, tf_Password.getText());
                        cstmt1.setString(4, tf_Email.getText());
                        cstmt1.setString(5, tf_Name.getText());
                        cstmt1.setString(6, tf_Surname.getText());
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if(success == 0){
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Registration Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Successful sign up!");
                            alert.showAndWait();
                            try {
                                new Swapper(false, stage, user, null, null, "startup/loginFXML", null);
                            } catch (IOException e) {
                                e.printStackTrace();
                                return;
                            }
                        }else if(success == 1){
                            lb_Error.setText("Nazwa użytkownika zajęta!");
                            lb_Error.setVisible(true);
                        }
                    }catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Registration Error");
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
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, null, null, null, "startup/loginFXML", null);
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
