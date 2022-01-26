/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uSelectReliefCountFXController implements Initializable {

    @FXML
    private Label lb_Name;
    @FXML
    private Label lb_Price;
    @FXML
    private Label lb_TotalCost;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<Map<String, Object>> tv_TableUlgi;
    @FXML
    private TableView<Map<String, Object>> tv_TableWybraneUlgi;
    @FXML
    private TableColumn<Map, String> tc_UlgiNazwa;
    @FXML
    private TableColumn<Map, String> tc_UlgiZnizka;
    @FXML
    private TableColumn<Map, String> tc_WybraneNazwa;
    @FXML
    private TableColumn<Map, String> tc_WybraneIlosc;
    @FXML
    private TableColumn<Map, String> tc_WybraneKoszt;

    private Stage stage = null;
    private User user = null;
    private ObservableList<Map<String, Object>> tableUlgi_items = FXCollections.<Map<String, Object>>observableArrayList();
    private ObservableList<Map<String, Object>> tableWybrane_items = FXCollections.<Map<String, Object>>observableArrayList();

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_UlgiNazwa.setCellValueFactory(new MapValueFactory<>("ulgi_nazwa"));
        tc_UlgiZnizka.setCellValueFactory(new MapValueFactory<>("ulgi_znizka"));
        tc_WybraneNazwa.setCellValueFactory(new MapValueFactory<>("wybrane_nazwa"));
        tc_WybraneIlosc.setCellValueFactory(new MapValueFactory<>("wybrane_ilosc"));
        tc_WybraneKoszt.setCellValueFactory(new MapValueFactory<>("wybrane_koszt"));
    }

    public void initTables() {
        tableUlgi_items.clear();
        tableWybrane_items.clear();
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
            try (
                    PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM rodzaje_biletow")
                    ;
            ) {
//                ResultSet rs1 = pstmt1.executeQuery();
//                while (rs1.next()) {
//                    Map<String, Object> item = new HashMap<>();
//                    item.put("ulgi_nazwa", rs1.getString(1));
//                    item.put("ulgi_znizka", rs1.getString(2));
//                    tableUlgi_items.add(item);
//                }
//                rs1.close();
//                ResultSet rs2 = pstmt1.executeQuery();
//                while (rs2.next()) {
//                    Map<String, Object> item = new HashMap<>();
//                    item.put("wybrane_nazwa", rs2.getString(1));
//                    item.put("wybrane_ilosc", rs2.getString(2));
//                    item.put("wybrane_koszt", rs2.getFloat(3));
//                    tableWybrane_items.add(item);
//                }
//                rs2.close();
//                tv_TableUlgi.setItems(tableUlgi_items);
//                tv_TableWybraneUlgi.setItems(tableWybrane_items);
            } catch (SQLException ex) {
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

    public void myInitialize(Map<String, Object> record) {
        lb_Name.setText((String) record.get("bilety_nazwa"));
        lb_Price.setText((String) record.get("bilety_cena"));
    }

    @FXML
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            String pattern = tf_Pattern.getText();
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
                try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM rodzaje_biletow WHERE TO_CHAR(nazwa) LIKE '%'||?||'%'");) {
//                    pstmt1.setString(1, pattern);
//                    ResultSet rs = pstmt1.executeQuery();
//                    tableUlgi_items.clear();
//                    while (rs.next()) {
//                        ResultSet rs1 = pstmt1.executeQuery();
//                        while (rs1.next()) {
//                            Map<String, Object> item = new HashMap<>();
//                            item.put("ulgi_nazwa", rs1.getString(1));
//                            item.put("ulgi_znizka", rs1.getString(2));
//                            tableUlgi_items.add(item);
//                        }
//                    }
//                    rs.close();
//                    tv_TableUlgi.setItems(tableUlgi_items);
                } catch (SQLException ex) {
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
    }

    @FXML
    private void addUlga(MouseEvent event) {
        if(event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if(event.getClickCount() == 2) {
                /*
                ADD THE RECORD IF POSSIBLE
                 */
                /*
                UPDATE TotalCost LABEL
                 */
            }
        }
    }

    @FXML
    private void deleteRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                /*
                CHECK IF THE RECORD IS SELECTED
                 */
                Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                ((sSureFXController) swapper.getController()).myInitialize(this, null/*RECORD*/, null, null, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void addToCart(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORDS IF POSSIBLE
            */
            back(event);
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
