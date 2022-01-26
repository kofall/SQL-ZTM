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
public class aInsertUpdateBiletyFXController implements Initializable {

    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_Time;
    @FXML
    private TextField tf_Area;
    @FXML
    private TextField tf_Price;

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

    public void myInitialize(Object controller, Map<String,Object> record) {
        prevController = controller;
        if(record!=null){
            tf_name.setText((String) record.get("nazwa"));
            tf_Time.setText( ((Integer)record.get("czas_obow")).toString());
            tf_Price.setText( ((Float)record.get("cena")).toString());
            tf_Area.setText((String) record.get("strefa"));
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
            if(tf_name.getText().equals("")||tf_Time.getText().equals("")||tf_Area.getText().equals("") || tf_Price.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(tf_Area.getText().length() !=1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Strefa może być tylko jedno literowa lub być cyfrą!");
                alert.showAndWait();
            }else {
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajRodzajBiletu(?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2, tf_name.getText());
                        cstmt1.setInt(3, Integer.parseInt(tf_Time.getText()));
                        cstmt1.setString(4, tf_Area.getText());
                        cstmt1.setFloat(5, Float.parseFloat(tf_Price.getText()));

                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie rodzaju biletów!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje rodzaj o takiej nazwie!");
                            alert.showAndWait();

                        }
                    } catch (SQLException ex) {
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
            }
            refreshTables();

        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(tf_name.getText().equals("")||tf_Time.getText().equals("")||tf_Area.getText().equals("") || tf_Price.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(tf_Area.getText().length() !=1){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Strefa może być tylko jedno literowa lub być cyfrą!");
                alert.showAndWait();
            }else {
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call zmodyfikujRodzajBiletu(?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2, tf_name.getText());
                        cstmt1.setInt(3, Integer.parseInt(tf_Time.getText()));
                        cstmt1.setString(4, tf_Area.getText());
                        cstmt1.setFloat(5, Float.parseFloat(tf_Price.getText()));

                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Modify Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udana modyfikacja rodzaju biletów!");
                            alert.showAndWait();
                        } else if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Nie istnieje rodzaj o takiej nazwie!");
                            alert.showAndWait();

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
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
