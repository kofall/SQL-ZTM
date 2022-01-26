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
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uUserBiletyFXController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tv_Table;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableColumn<Map, String> tc_Nazwa;
    @FXML
    private TableColumn<Map, String> tc_CzasObowiazywania;
    @FXML
    private TableColumn<Map, String> tc_Strefa;
    @FXML
    private TableColumn<Map, String> tc_Cena;

    private Stage stage = null;
    private User user = null;
    private ObservableList<Map<String, Object>> table_items = FXCollections.<Map<String, Object>>observableArrayList();

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_Nazwa.setCellValueFactory(new MapValueFactory<>("nazwa"));
        tc_CzasObowiazywania.setCellValueFactory(new MapValueFactory<>("czas_obowiazywania"));
        tc_Strefa.setCellValueFactory(new MapValueFactory<>("strefa"));
        tc_Cena.setCellValueFactory(new MapValueFactory<>("cena"));
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM rodzaje_biletow");) {
                ResultSet rs = pstmt1.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("nazwa", rs.getString(1));
                    item.put("czas_obowiazywania", rs.getString(2));
                    item.put("strefa", rs.getString(3));
                    item.put("cena", rs.getFloat(4));
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
                    pstmt1.setString(1, pattern);
                    ResultSet rs = pstmt1.executeQuery();
                    table_items.clear();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("nazwa", rs.getString(1));
                        item.put("czas_obowiazywania", rs.getString(2));
                        item.put("strefa", rs.getString(3));
                        item.put("cena", rs.getFloat(4));
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
    private void swapKoszyk(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapHistoria(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userHistoriaFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapUlgi(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userUlgiFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapLinie(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userLinieFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
