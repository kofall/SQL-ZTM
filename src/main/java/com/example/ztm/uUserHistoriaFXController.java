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

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.MapValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uUserHistoriaFXController implements Initializable {

    @FXML
    private TableView<Map<String, Object>> tv_Table;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableColumn<Map, String> tc_Id;
    @FXML
    private TableColumn<Map, String> tc_Skasowany;
    @FXML
    private TableColumn<Map, String> tc_Data;
    @FXML
    private TableColumn<Map, String> tc_Opis;


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
        tc_Id.setCellValueFactory(new MapValueFactory<>("id"));
        tc_Skasowany.setCellValueFactory(new MapValueFactory<>("skasowany"));
        tc_Data.setCellValueFactory(new MapValueFactory<>("data"));
        tc_Opis.setCellValueFactory(new MapValueFactory<>("opis"));
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM wpis_historii INNER JOIN bilet ON wpis_historii.bilet_id_biletu = bilet.id_biletu WHERE wpis_historii.konto_id_konta=?");) {
                pstmt1.setInt(1, user.getIdKonta());
                ResultSet rs = pstmt1.executeQuery();
                while (rs.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("id", rs.getInt(6));
                    String opis = rs.getString(8);
                    if(opis != null)
                        item.put("skasowany", "Tak");
                    else
                        item.put("skasowany", "Nie");
                    item.put("data", rs.getString(2));
                    item.put("opis", rs.getString(3));
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
                try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM wpis_historii INNER JOIN bilet ON wpis_historii.bilet_id_biletu = bilet.id_biletu WHERE bilet_id_biletu=? AND wpis_historii.konto_id_konta=?");) {
                    pstmt1.setString(1, pattern);
                    pstmt1.setInt(2, user.getIdKonta());
                    ResultSet rs = pstmt1.executeQuery();
                    table_items.clear();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("id", rs.getInt(6));
                        String opis = rs.getString(8);
                        if(opis != null)
                            item.put("skasowany", "Tak");
                        else
                            item.put("skasowany", "Nie");
                        item.put("data", rs.getString(2));
                        item.put("opis", rs.getString(3));
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
    private void validate(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Select Error");
                alert.setHeaderText(null);
                alert.setContentText("Rekord nie został wybrany!");
                alert.showAndWait();
            } else {
                Map<String, Object> record = selectedItems.get(0);
                if(((String) ((Map<String,Object>) record).get("skasowany")).equals("Nie")) {
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

                                CallableStatement cstmt1 = conn.prepareCall("{? = call skasujBilet(?,?)}");
                                ) {

                            cstmt1.registerOutParameter(1, Types.INTEGER);
                            cstmt1.setInt(2, user.getIdKonta());
                            cstmt1.setInt(3, (Integer)record.get("id"));
                            cstmt1.execute();
                            int success = cstmt1.getInt(1);
                            if (success == 1) {
                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                alert.setTitle("Insert Information");
                                alert.setHeaderText(null);
                                alert.setContentText("Skasowano bilet!");
                                alert.showAndWait();
                                initTables();
                            } else if (success == 0) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Insert Error");
                                alert.setHeaderText(null);
                                alert.setContentText("Bilet został już skasowany!");
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
    private void swapForMoreInfo(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getClickCount() == 2) {
                try {
                    ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
                    if (selectedItems.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Select Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Bilet nie został wybrany!");
                        alert.showAndWait();
                    } else {
                        Map<String, Object> record = selectedItems.get(0);
                        Swapper swapper = new Swapper(true, stage, user, null, null, "user/userHistoriaBiletInfoFXML", null);
                        ((uUserHistoriaBiletInfoFXController) swapper.getController()).myInitialize(record);
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
