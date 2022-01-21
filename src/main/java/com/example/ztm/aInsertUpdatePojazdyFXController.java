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
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aInsertUpdatePojazdyFXController implements Initializable {

    @FXML
    private TextField tf_Serial;
    @FXML
    private TextField tf_Registration;
    @FXML
    private TextField tf_StandingPlaces;
    @FXML
    private TextField tf_Seats;
    @FXML
    private TextField tf_Type;
    @FXML
    private RadioButton rb_Bus;
    @FXML
    private RadioButton rb_Tram;

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
        if(record != null){
            tf_Serial.setText((String) record.get("nr_seryjny"));
            tf_Registration.setText((String) record.get("nr_rejestracyjny"));
            tf_Seats.setText((String) record.get("siedzace").toString());
            tf_StandingPlaces.setText((String) record.get("stojace").toString());
            String type = (String) record.get("typ");
            if (type.equals("Autobus")){
                rb_Bus.setSelected(true);
                rb_Tram.setSelected(false);
            }else if (type.equals("Tramwaj")){
                rb_Tram.setSelected(true);
                rb_Bus.setSelected(false);
            }
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
    private void updateRBs(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(event.getSource() == rb_Bus) {
                rb_Bus.setSelected(true);
                rb_Tram.setSelected(false);
            }
            else {
                rb_Bus.setSelected(false);
                rb_Tram.setSelected(true);
            }
        }
    }

    @FXML
    private void addRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if (tf_Serial.getText().equals("") || tf_StandingPlaces.getText().equals("") || tf_Seats.getText().equals("")
                    || (rb_Bus.isSelected() && tf_Registration.getText().equals(""))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            } else {

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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajPojazd(?,?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2, tf_Serial.getText());
                        cstmt1.setInt(3, Integer.parseInt(tf_StandingPlaces.getText()));
                        cstmt1.setInt(4, Integer.parseInt(tf_Seats.getText()));
                        if (rb_Bus.isSelected()) {
                            cstmt1.setString(5, "A");
                        } else if (rb_Tram.isSelected()) {
                            cstmt1.setString(5, "T");
                        }
                        cstmt1.setString(6, tf_Registration.getText());

                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie pojazdu!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje pojazd o takim numerze seryjnym!");
                            alert.showAndWait();

                        } else if (success == 2) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Brakujący numer rejestracyjny dla autobusu!");
                            alert.showAndWait();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();

                    } catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Podano nie numeryczne wartości w polu dotyczącym miejsc siedzących lub stojących!");
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
        refreshTables();
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if (tf_Serial.getText().equals("") || tf_StandingPlaces.getText().equals("") || tf_Seats.getText().equals("")
                    || (rb_Bus.isSelected() && tf_Registration.getText().equals(""))) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            } else {

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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call zmodyfikujPojazd(?,?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setString(2, tf_Serial.getText());
                        cstmt1.setInt(3, Integer.parseInt(tf_StandingPlaces.getText()));
                        cstmt1.setInt(4, Integer.parseInt(tf_Seats.getText()));
                        if(rb_Bus.isSelected()){
                            cstmt1.setString(5,"A");
                        }else if (rb_Tram.isSelected()){
                            cstmt1.setString(5,"T");
                        }
                        cstmt1.setString(6, tf_Registration.getText());

                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Modify Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie pojazdu!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje pojazd o takim numerze seryjnym!");
                            alert.showAndWait();

                        } else if (success == 2) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Brakujący numer rejestracyjny dla autobusu!");
                            alert.showAndWait();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();

                    } catch (NumberFormatException ex){
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Podano nie numeryczne wartości w polu dotyczącym miejsc siedzących lub stojących!");
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
