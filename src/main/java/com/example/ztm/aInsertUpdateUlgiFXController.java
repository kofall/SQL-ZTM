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
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aInsertUpdateUlgiFXController implements Initializable {

    @FXML
    private TextField tf_Percent;
    @FXML
    private TextField tf_Name;

    private Stage stage = null;
    private User user = null;
    private Object prevController = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object controller, Map<String, Object> record) {
        prevController = controller;
        if(record!=null){
            tf_Name.setText((String) record.get("nazwa"));
            tf_Percent.setText((String) record.get("wartosc_procentowa"));
        }
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
    private void addRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(tf_Name.getText().equals("")|| tf_Percent.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else{
                Connection conn = null;
                String connectionString =
                        "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/" +
                                "dblab02_students.cs.put.poznan.pl";
                Properties connectionProps = new Properties();
                connectionProps.put("user", "inf145326");
                connectionProps.put("password", "inf145326");
                try {
                    conn = DriverManager.getConnection(connectionString,
                            connectionProps);
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajUlge(?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2,tf_Name.getText());
                        cstmt1.setFloat(3,Float.parseFloat(tf_Percent.getText()));
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie ulgi!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje już ulga o takiej nazwie!");
                            alert.showAndWait();

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();

                    }catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Podano nie numeryczne wartości w polu dotyczącym wartości procentowej!");
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
            refreshTables();
        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(tf_Name.getText().equals("")|| tf_Percent.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else{
                Connection conn = null;
                String connectionString =
                        "jdbc:oracle:thin:@//admlab2.cs.put.poznan.pl:1521/" +
                                "dblab02_students.cs.put.poznan.pl";
                Properties connectionProps = new Properties();
                connectionProps.put("user", "inf145326");
                connectionProps.put("password", "inf145326");
                try {
                    conn = DriverManager.getConnection(connectionString,
                            connectionProps);
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call zmodyfikujUlge(?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2,tf_Name.getText());
                        cstmt1.setFloat(3,Float.parseFloat(tf_Percent.getText()));
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Modify Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udana modyfikacja ulgi!");
                            alert.showAndWait();
                        } else if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Nie istnieje ulga o takiej nazwie!");
                            alert.showAndWait();

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();

                    }catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Podano nie numeryczne wartości w polu dotyczącym wartości procentowej!");
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
            refreshTables();
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            refreshTables();
            stage.close();
        }
    }
}
