/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
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
public class aInsertUpdateGodzinyOdjazdowFXController implements Initializable {

    @FXML
    private TextField tf_Hour;
    @FXML
    private TextField tf_Minutes;

    private Stage stage = null;
    private User user = null;
    private Object prevController = null;
    private Integer id_szt;
    private Integer nr_lini;
    private Map<String, Object> prev_record;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }
    private ObservableList<String> przystanek_options =
            FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

    }

    public void myInitialize(Object controller, Map<String, Object> record, Integer nr) {
        prevController = controller;
        if(record!=null){
            String time = (String) record.get("godz");
            tf_Hour.setText(time.split(":")[0]);
            tf_Minutes.setText(time.split(":")[1]);
            prev_record = record;
        }
        id_szt = nr;

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
            if(tf_Hour.getText().equals("")|| tf_Minutes.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(!tf_Hour.getText().matches("([0-1][0-9]|[2][0-3])") ||
                    !tf_Minutes.getText().matches("([0-5][0-9])")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Błędny format godziny lub minut (Poprawny: hh lub mm)!");
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajGodzineOdjazdu(?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setInt(2, id_szt);
                        int year1 = 0;
                        int month1 = 0;
                        int day1 = 1;
                        int hour1 = Integer.parseInt(tf_Hour.getText());
                        int minutes1 = Integer.parseInt(tf_Minutes.getText());
                        Timestamp start = new Timestamp(year1, month1, day1,
                                hour1, minutes1, 0, 0);

                        cstmt1.setTimestamp(3 ,start);

                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie godziny odjazdu!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje już taka godzina odjazdu!");
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
                        alert.setContentText("Podano nie numeryczne wartości!");
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
            if(prev_record==null){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brak zaznaczonego rekordu, cofnij sie do poprzedniego ekranu i wybierz opcje zmodyfikuj!");
                alert.showAndWait();
            }
            else if(tf_Hour.getText().equals("")|| tf_Minutes.getText().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(!tf_Hour.getText().matches("([0-1][0-9]|[2][0-3])") ||
                    !tf_Minutes.getText().matches("([0-5][0-9])")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Błędny format godziny lub minut (Poprawny: hh lub mm)!");
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajGodzineOdjazdu(?,?)}");
                        PreparedStatement pstmt1 = conn.prepareStatement("DELETE FROM godziny_odjazdow WHERE ID_PRZYSTANKU_W_TRASIE_SZT=? AND godzina_odjazdu = ?")) {
                        conn.setAutoCommit(false);
                        pstmt1.setInt(1,id_szt);
                        pstmt1.setTimestamp(2,(Timestamp) prev_record.get("time"));
                        int delete_success =pstmt1.executeUpdate();
                        if(delete_success==1){
                            cstmt1.registerOutParameter(1, Types.INTEGER);
                            cstmt1.setInt(2, id_szt);
                            int year1 = 0;
                            int month1 = 0;
                            int day1 = 1;
                            int hour1 = Integer.parseInt(tf_Hour.getText());
                            int minutes1 = Integer.parseInt(tf_Minutes.getText());
                            Timestamp start = new Timestamp(year1, month1, day1,
                                    hour1, minutes1, 0, 0);

                            cstmt1.setTimestamp(3 ,start);

                            cstmt1.execute();
                            int success = cstmt1.getInt(1);
                            if (success == 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Modify Information");
                                alert.setHeaderText(null);
                                alert.setContentText("Udana modyfikacja godziny odjazdu!");
                                alert.showAndWait();
                                conn.commit();
                            } else if (success == 1) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Modify Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Istnieje taka godzina odjazdu!");
                                alert.showAndWait();
                                conn.rollback();
                            }
                        }else{
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Nie istnieje modyfikowana godzina odjazdu!");
                            alert.showAndWait();
                            conn.rollback();
                        }

                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();
                        conn.rollback();
                    }catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Podano nie numeryczne wartości!");
                        alert.showAndWait();
                        conn.rollback();
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
