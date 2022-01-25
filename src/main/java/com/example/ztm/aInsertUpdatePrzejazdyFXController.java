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
    private Map<String, Object> prev_record;
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
                 PreparedStatement pstmt3 = conn.prepareStatement("SELECT nr_lini FROM linia");)
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
            cb_Line.setValue((String) record.get("linia_nr").toString());
            cb_Vehicle.setValue((String) record.get("pojazd"));
            cb_Driver.setValue((String) record.get("kierowca"));
            dp_Start.setValue(((Timestamp) record.get("data_rozp")).toLocalDateTime().toLocalDate());
            dp_End.setValue(((Timestamp) record.get("data_zak")).toLocalDateTime().toLocalDate());
            tf_Godz_rozp.setText(String.format("%02d",((Timestamp) record.get("data_rozp")).toLocalDateTime().getHour()) +":"+String.format("%02d",((Timestamp) record.get("data_rozp")).toLocalDateTime().getMinute() ));
            tf_Godz_zak.setText(String.format("%02d",((Timestamp) record.get("data_zak")).toLocalDateTime().getHour()) +":"+String.format("%02d",((Timestamp) record.get("data_rozp")).toLocalDateTime().getMinute() ));
            prev_record = record;
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
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(!tf_Godz_rozp.getText().matches("([0-1][0-9]|[2][0-3]):([0-5][0-9])") ||
                !tf_Godz_zak.getText().matches("([0-1][0-9]|[2][0-3]):([0-5][0-9])")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Błędny format godziny (Poprawny: hh:mm)!");
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
                        try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajPrzejazd(?,?,?,?,?)}");) {
                            cstmt1.registerOutParameter(1, Types.INTEGER);
                            LocalDate start_d = dp_Start.getValue();
                            LocalDate end_d = dp_End.getValue();
                            int year1 = start_d.getYear()-1900;
                            int month1 = start_d.getMonthValue()- 1;
                            int day1 = start_d.getDayOfMonth() ;
                            int hour1 = Integer.parseInt(tf_Godz_rozp.getText().split(":")[0]);
                            int minutes1 = Integer.parseInt(tf_Godz_rozp.getText().split(":")[1]);
                            Timestamp start = new Timestamp(year1, month1, day1,
                                    hour1, minutes1, 0, 0);

                            cstmt1.setTimestamp(2 ,start);
                            int year2 = end_d.getYear()-1900;
                            int month2 = end_d.getMonthValue() - 1;
                            int day2 = end_d.getDayOfMonth();
                            int hour2 = Integer.parseInt(tf_Godz_zak.getText().split(":")[0]);
                            int minutes2 = Integer.parseInt(tf_Godz_zak.getText().split(":")[1]);
                            Timestamp end = new Timestamp(year2, month2, day2,
                                    hour2, minutes2, 0, 0);
                            cstmt1.setTimestamp(3 ,end);
                            cstmt1.setString(4,(String) cb_Driver.getSelectionModel().getSelectedItem());
                            cstmt1.setString(5,(String) cb_Vehicle.getSelectionModel().getSelectedItem());
                            cstmt1.setInt(6, Integer.parseInt((String)cb_Line.getSelectionModel().getSelectedItem()));
                            cstmt1.execute();
                            int success = cstmt1.getInt(1);
                            if (success == 0) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Insert Information");
                                alert.setHeaderText(null);
                                alert.setContentText("Udane dodanie przejazdu!");
                                alert.showAndWait();
                            } else if (success == 1) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Insert Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Istnieje już taki przejazd!");
                                alert.showAndWait();

                            }else if (success == 2) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Insert Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Niepoprawne daty, data zakonczenia powinna być większa niż data rozpoczęcia!");
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
                            alert.setContentText("Podano nie numeryczne wartości w polu dotyczącym numeru w trasie!");
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
            if(tf_Godz_rozp.getText().equals("")|| tf_Godz_zak.getText().equals("") ||
                    cb_Driver.getSelectionModel().getSelectedItem().equals("Kierowca") ||
                    cb_Vehicle.getSelectionModel().getSelectedItem().equals("Pojazd") ||
                    cb_Line.getSelectionModel().getSelectedItem().equals("Linia") || dp_Start.toString().equals("") ||
                    dp_End.toString().equals("")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else if(!tf_Godz_rozp.getText().matches("([0-1][0-9]|[2][0-3]):([0-5][0-9])") ||
                    !tf_Godz_zak.getText().matches("([0-1][0-9]|[2][0-3]):([0-5][0-9])")) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Błędny format godziny (Poprawny: hh:mm)!");
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajPrzejazd(?,?,?,?,?)}");
                    PreparedStatement pstmt1 = conn.prepareStatement(
                            "DELETE FROM przejazdy WHERE data_rozpoczecia = ? AND DATA_ZAKONCZENIA = ? AND KIEROWCA_PESEL=? AND POJAZD_NUMER_SERYJNY=? AND LINIA_NR_LINI=?")) {
                        conn.setAutoCommit(false);
                        pstmt1.setTimestamp(1,(Timestamp) prev_record.get("data_rozp"));
                        pstmt1.setTimestamp(2,(Timestamp) prev_record.get("data_zak"));
                        pstmt1.setString(3, (String) prev_record.get("kierowca"));
                        pstmt1.setString(4, (String) prev_record.get("pojazd"));
                        pstmt1.setInt(5, (Integer) prev_record.get("linia_nr"));
                        pstmt1.execute();
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        LocalDate start_d = dp_Start.getValue();
                        LocalDate end_d = dp_End.getValue();
                        int year1 = start_d.getYear()-1900;
                        int month1 = start_d.getMonthValue()- 1;
                        int day1 = start_d.getDayOfMonth() ;
                        int hour1 = Integer.parseInt(tf_Godz_rozp.getText().split(":")[0]);
                        int minutes1 = Integer.parseInt(tf_Godz_rozp.getText().split(":")[1]);
                        Timestamp start = new Timestamp(year1, month1, day1,
                                hour1, minutes1, 0, 0);

                        cstmt1.setTimestamp(2 ,start);
                        int year2 = end_d.getYear()-1900;
                        int month2 = end_d.getMonthValue() - 1;
                        int day2 = end_d.getDayOfMonth();
                        int hour2 = Integer.parseInt(tf_Godz_zak.getText().split(":")[0]);
                        int minutes2 = Integer.parseInt(tf_Godz_zak.getText().split(":")[1]);
                        Timestamp end = new Timestamp(year2, month2, day2,
                                hour2, minutes2, 0, 0);
                        cstmt1.setTimestamp(3 ,end);
                        cstmt1.setString(4,(String) cb_Driver.getSelectionModel().getSelectedItem());
                        cstmt1.setString(5,(String) cb_Vehicle.getSelectionModel().getSelectedItem());
                        cstmt1.setInt(6, Integer.parseInt((String)cb_Line.getSelectionModel().getSelectedItem()));
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udana modyfikacja przejazdu!");
                            alert.showAndWait();
                            conn.commit();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje już taki przejazd!");
                            alert.showAndWait();
                            conn.rollback();
                        }else if (success == 2) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Niepoprawne daty, data zakonczenia powinna być większa niż data rozpoczęcia!");
                            alert.showAndWait();
                            conn.rollback();
                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
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
