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
    private String pkName;
    private String tableName;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }
    boolean przejazdy = false;
    boolean godz = false;
    private Integer id_szt;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object controller, Map<String,Object> record, String key, String pk, String table) {
        prevController = controller;
        recordV = record;
        keyV = key;
        pkName = pk;
        tableName = table;
        l_message.setText("Czy na pewno chcesz usunąć rekord o kluczu głównym: "+((String) record.get(key))+"?");
    }
    public void myInitialize(Object controller, Map<String,Object> record) {
        prevController = controller;
        recordV = record;
        przejazdy=true;
        l_message.setText("Czy na pewno chcesz usunąć ten przejazd?");
    }
    public void myInitialize(Object controller, Map<String,Object> record,Integer id) {
        prevController = controller;
        recordV = record;
        godz=true;
        id_szt = id;
        l_message.setText("Czy na pewno chcesz usunąć tą godzinę?");
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
            if(!przejazdy && !godz){
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
                    String sql_statement = "DELETE FROM "+tableName+" WHERE "+pkName+" = ?";
                    try (PreparedStatement pstmt1 = conn.prepareStatement(sql_statement);){

                        if (recordV.get(keyV) instanceof String){
                            pstmt1.setString(1,(String)recordV.get(keyV));
                        }else if (recordV.get(keyV) instanceof Integer){
                            pstmt1.setInt(1, (Integer)recordV.get(keyV));
                        }
                        pstmt1.execute();

                    }catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Delete Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nie udało się usunąć rekordu!");
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
            }else if(przejazdy &&!godz){
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
                    String sql_statement = "DELETE FROM przejazdy WHERE  data_rozpoczecia = ? AND DATA_ZAKONCZENIA = ? AND KIEROWCA_PESEL= ? AND POJAZD_NUMER_SERYJNY= ? AND LINIA_NR_LINI= ?";
                    try (PreparedStatement pstmt1 = conn.prepareStatement(sql_statement);){

                        pstmt1.setTimestamp(1,(Timestamp) recordV.get("data_rozp"));
                        pstmt1.setTimestamp(2,(Timestamp) recordV.get("data_zak"));
                        pstmt1.setString(3, (String) recordV.get("kierowca"));
                        pstmt1.setString(4, (String) recordV.get("pojazd"));
                        pstmt1.setInt(5, (Integer) recordV.get("linia_nr"));
                        pstmt1.execute();


                    }catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Delete Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nie udało się usunąć rekordu!");
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
            }else if(godz && !przejazdy){

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
                    String sql_statement = "DELETE FROM godziny_odjazdow WHERE ID_PRZYSTANKU_W_TRASIE_SZT=? AND godzina_odjazdu = ?";
                    try (PreparedStatement pstmt1 = conn.prepareStatement(sql_statement);){
                        pstmt1.setInt(1,id_szt);
                        pstmt1.setTimestamp(2,(Timestamp) recordV.get("time"));
                        pstmt1.execute();


                    }catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Delete Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nie udało się usunąć rekordu!");
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
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            refreshTables();
            stage.close();
        }
    }
}
