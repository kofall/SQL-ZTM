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
public class uUserLinieFXController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tv_Table;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableColumn<Map, String> tc_Nr;
    @FXML
    private TableColumn<Map, String> tc_Godzina_rozp;
    @FXML
    private TableColumn<Map, String> tc_Godzina_zak;


    private Stage stage = null;
    private User user = null;
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
        tc_Nr.setCellValueFactory(new MapValueFactory<>("nr"));
        tc_Godzina_rozp.setCellValueFactory(new MapValueFactory<>("godz_rozp"));
        tc_Godzina_zak.setCellValueFactory(new MapValueFactory<>("godz_zak"));
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM linia");) {
                ResultSet rs = pstmt1.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("nr", rs.getInt(1));
                    String godz_dzialania = rs.getString(2);
                    item.put("godz_rozp", godz_dzialania.split("-")[0]);
                    item.put("godz_zak", godz_dzialania.split("-")[1]);
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
                try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM linia WHERE TO_CHAR(nr_lini) LIKE '%'||?||'%'");) {
                    pstmt1.setString(1, pattern);
                    ResultSet rs = pstmt1.executeQuery();
                    table_items.clear();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("nr", rs.getInt(1));
                        String godz_dzialania = rs.getString(2);
                        item.put("godz_rozp", godz_dzialania.split("-")[0]);
                        item.put("godz_zak", godz_dzialania.split("-")[1]);
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
                Swapper swapper = new Swapper(false, stage, user, null, null, "user/buyTicketFXML", null);
                ((uBuyTicketFXController) swapper.getController()).myInitialize(true);
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
    private void swapBilety(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userBiletyFXML", null);
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
    private void swapTrasa(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getClickCount() == 2) {
                try {
                    ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
                    if (selectedItems.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Select Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Linia nie zosta??a wybrana!");
                        alert.showAndWait();
                    } else {
                        Map<String,Object> record = selectedItems.get(0);
                        Swapper swapper = new Swapper(false, stage, user, null, null, "user/trasaFXML", null);
                        ((uTrasaFXController) swapper.getController()).myInitialize(record, "userLinieFXML");
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
                new Swapper(false, stage, user, null, null, "user/userFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}