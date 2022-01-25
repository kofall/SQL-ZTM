/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aInsertUpdatePrzejazdyFXController implements Initializable {

    @FXML
    private ComboBox cb_Line;
    @FXML
    private ComboBox cb_Driver;
    @FXML
    private ComboBox cb_Vehicle;
    @FXML
    private DatePicker dp_Start;
    @FXML
    private DatePicker dp_End;
    @FXML
    private TextField tf_Godz_rozp;
    @FXML
    private TextField tf_Godz_zak;

    private Stage stage = null;
    private User user = null;
    private Object prevController = null;
    private ObservableList<String> pojazd_options =
            FXCollections.observableArrayList();
    private ObservableList<String> kierowca_options =
            FXCollections.observableArrayList();
    private ObservableList<String> linia_options =
            FXCollections.observableArrayList();
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dp_Start.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd-MM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dp_Start.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
        dp_End.setConverter(new StringConverter<LocalDate>() {
            String pattern = "dd-MM-yyyy";
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(pattern);

            {
                dp_End.setPromptText(pattern.toLowerCase());
            }

            @Override public String toString(LocalDate date) {
                if (date != null) {
                    return dateFormatter.format(date);
                } else {
                    return "";
                }
            }

            @Override public LocalDate fromString(String string) {
                if (string != null && !string.isEmpty()) {
                    return LocalDate.parse(string, dateFormatter);
                } else {
                    return null;
                }
            }
        });
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT numer_seryjny FROM pojazd");
                 PreparedStatement pstmt2 = conn.prepareStatement("SELECT pesel FROM kierowca");
                 PreparedStatement pstmt3 = conn.prepareStatement("SELECT nr_linii FROM linia");)
            {
                ResultSet rs1 = pstmt1.executeQuery();
                while(rs1.next()){
                    pojazd_options.add(rs1.getString(1));
                }
                rs1.close();
                cb_Vehicle.setItems(pojazd_options);
                ResultSet rs2 = pstmt2.executeQuery();
                while(rs2.next()){
                    kierowca_options.add(rs2.getString(1));
                }
                rs2.close();
                cb_Driver.setItems(kierowca_options);
                ResultSet rs3 = pstmt3.executeQuery();
                while(rs3.next()){
                    linia_options.add(rs3.getString(1));
                }
                rs3.close();
                cb_Line.setItems(linia_options);
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

    public void myInitialize(Object controller, Map<String, Object> record) {
        prevController = controller;
        if (record!=null){
            cb_Line.setValue((String) record.get("linia_nr"));
            cb_Vehicle.setValue((String) record.get("pojazd"));
            cb_Driver.setValue((String) record.get("kierowca"));
            dp_Start.setValue(((Timestamp) record.get("data_rozp")).toLocalDateTime().toLocalDate());
            dp_End.setValue(((Timestamp) record.get("data_zak")).toLocalDateTime().toLocalDate());
            tf_Godz_rozp.setText(((Timestamp) record.get("data_rozp")).toLocalDateTime().getHour() +":"+((Timestamp) record.get("data_rozp")).toLocalDateTime().getMinute() );
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
            if(tf_Godz_rozp.getText().equals("")|| tf_Godz_zak.getText().equals("") ||
                    cb_Driver.getSelectionModel().getSelectedItem().equals("Kierowca") ||
                    cb_Vehicle.getSelectionModel().getSelectedItem().equals("Pojazd") ||
                    cb_Line.getSelectionModel().getSelectedItem().equals("Linia") || dp_Start.toString().equals("") ||
                    dp_End.toString().equals("")){

            }

        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            MODIFY THE RECORD IF EXISTS
             */
            back(event);
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
