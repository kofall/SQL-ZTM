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
public class uTrasaFXController implements Initializable {

    @FXML
    private Label lb_LineNumber;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<Map<String, Object>> tv_Table;
    @FXML
    private TableColumn<Map, String> tc_Nr_w_trasie;
    @FXML
    private TableColumn<Map, String> tc_Nazwa;
    @FXML
    private TableColumn<Map, String> tc_na_zadanie;

    private Map<String, Object> record = null;
    private Integer nr_linii;
    private Stage stage = null;
    private User user = null;
    private String backFXML = null;
    private ObservableList<Map<String, Object>> table_items = FXCollections.<Map<String, Object>>observableArrayList();

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_Nazwa.setCellValueFactory(new MapValueFactory<>("nazwa"));
        tc_Nr_w_trasie.setCellValueFactory(new MapValueFactory<>("nr_w_trasie"));
        tc_na_zadanie.setCellValueFactory(new MapValueFactory<>("na_zadanie"));
    }

    public void initTables() {
        table_items.clear();
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT nr_w_trasie, nazwa, na_zadanie,id_przystanku_w_trasie_szt FROM przystanek_w_trasie INNER JOIN przystanek ON przystanek.nazwa = przystanek_w_trasie.przystanek_nazwa WHERE linia_nr_lini =?");) {
                pstmt1.setInt(1, nr_linii);
                ResultSet rs = pstmt1.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("nr_w_trasie", rs.getInt(1));
                    item.put("nazwa", rs.getString(2));
                    String na_zadanie = rs.getString(3);
                    if (na_zadanie.equals("T")) {
                        item.put("na_zadanie", "Tak");
                    } else if (na_zadanie.equals("N")) {
                        item.put("na_zadanie", "Nie");
                    }

                    item.put("id", rs.getInt(4));
                    table_items.add(item);
                }
                rs.close();
                tv_Table.setItems(table_items);
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

    public void myInitialize(Map<String, Object> record, String backFXML) {
        this.backFXML = backFXML;
        this.record = record;
        nr_linii = (Integer) record.get("nr");
        lb_LineNumber.setText(nr_linii.toString());
        initTables();
    }

    @FXML
    private void find(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
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
                try (PreparedStatement pstmt1 = conn.prepareStatement(
                        "SELECT nr_w_trasie, nazwa, na_zadanie,id_przystanku_w_trasie_szt FROM przystanek_w_trasie INNER JOIN przystanek ON przystanek.nazwa = przystanek_w_trasie.przystanek_nazwa WHERE linia_nr_lini =? AND TO_CHAR(nr_w_trasie) LIKE '%'||?||'%'");) {
                    pstmt1.setInt(1, nr_linii);
                    pstmt1.setString(2, pattern);
                    ResultSet rs = pstmt1.executeQuery();
                    table_items.clear();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("nr_w_trasie", rs.getInt(1));
                        item.put("nazwa", rs.getString(2));
                        String na_zadanie = rs.getString(3);
                        if (na_zadanie.equals("T")) {
                            item.put("na_zadanie", "Tak");
                        } else if (na_zadanie.equals("N")) {
                            item.put("na_zadanie", "Nie");
                        }
                        item.put("id", rs.getInt(4));
                        table_items.add(item);
                    }
                    rs.close();
                    tv_Table.setItems(table_items);
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
    private void swapGodziny(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getClickCount() == 2) {
                try {
                    ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
                    if (selectedItems.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Select Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Przystanek nie został wybrany!");
                        alert.showAndWait();
                    } else {
                        Map<String,Object> record = selectedItems.get(0);
                        Swapper swapper = new Swapper(false, stage, user, null, null, "user/trasaGodzinyFXML", null);
                        ((uTrasaGodzinyFXController) swapper.getController()).myInitialize(record,this.record, backFXML,nr_linii);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/" + backFXML, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
