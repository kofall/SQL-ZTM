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
public class uBuyTicketFXController implements Initializable {

    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<Map<String, Object>> tv_TableBilety;
    @FXML
    private TableView<Map<String, Object>> tv_TableWybraneBilety;
    @FXML
    private Label lb_TotalCost;
    @FXML
    private TableColumn<Map, String> tc_BiletyNazwa;
    @FXML
    private TableColumn<Map, String> tc_BiletyCzas;
    @FXML
    private TableColumn<Map, String> tc_BiletyStrefa;
    @FXML
    private TableColumn<Map, String> tc_BiletyCena;
    @FXML
    private TableColumn<Map, String> tc_WybraneNazwa;
    @FXML
    private TableColumn<Map, String> tc_WybraneUlga;
    @FXML
    private TableColumn<Map, String> tc_WybraneIlosc;
    @FXML
    private TableColumn<Map, String> tc_WybraneKoszt;

    private Stage stage = null;
    private User user = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }
    private ObservableList<Map<String, Object>> tableBilety_items = FXCollections.<Map<String, Object>>observableArrayList();

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_BiletyNazwa.setCellValueFactory(new MapValueFactory<>("bilety_nazwa"));
        tc_BiletyCzas.setCellValueFactory(new MapValueFactory<>("bilety_czas"));
        tc_BiletyStrefa.setCellValueFactory(new MapValueFactory<>("bilety_strefa"));
        tc_BiletyCena.setCellValueFactory(new MapValueFactory<>("bilety_cena"));
        tc_WybraneNazwa.setCellValueFactory(new MapValueFactory<>("wybrane_nazwa"));
        tc_WybraneUlga.setCellValueFactory(new MapValueFactory<>("wybrane_ulga"));
        tc_WybraneIlosc.setCellValueFactory(new MapValueFactory<>("wybrane_ilosc"));
        tc_WybraneKoszt.setCellValueFactory(new MapValueFactory<>("wybrane_koszt"));
    }

    public void initTables() {
        tableBilety_items.clear();
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
                    PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM rodzaje_biletow");
                    ) {
                ResultSet rs1 = pstmt1.executeQuery();
                while (rs1.next()) {
                    Map<String, Object> item = new HashMap<>();
                    item.put("bilety_nazwa", rs1.getString(1));
                    item.put("bilety_czas", rs1.getString(2));
                    item.put("bilety_strefa", rs1.getString(3));
                    item.put("bilety_cena", rs1.getFloat(4));
                    tableBilety_items.add(item);
                }
                rs1.close();
                tv_TableBilety.setItems(tableBilety_items);
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

        tv_TableWybraneBilety.setItems(user.getKoszyk_items());
    }

    public void myInitialize(Boolean clear) {
        if(clear) {
            user.getKoszyk_items().clear();
            lb_TotalCost.setText("0.00 zł");
        } else {
            float totalPrice = 0;
            for(Map<String, Object> record : user.getKoszyk_items()) {
                totalPrice += (float) record.get("wybrane_koszt");
            }
            lb_TotalCost.setText(String.valueOf(totalPrice) + " zł");
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
                    tableBilety_items.clear();
                    while (rs.next()) {
                        Map<String, Object> item = new HashMap<>();
                        item.put("nazwa", rs.getString(1));
                        item.put("czas_obowiazywania", rs.getString(2));
                        item.put("strefa", rs.getString(3));
                        item.put("cena", rs.getFloat(4));
                        tableBilety_items.add(item);
                    }
                    rs.close();
                    tv_TableBilety.setItems(tableBilety_items);
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
    private void deleteRecord(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            if(event.getSource() == tv_TableWybraneBilety)
                if(event.getClickCount() < 2)
                    return;
            ObservableList<Map<String, Object>> selectedItems = tv_TableWybraneBilety.getSelectionModel().getSelectedItems();
            if (selectedItems.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Delete Error");
                alert.setHeaderText(null);
                alert.setContentText("Produkt nie został wybrany!");
                alert.showAndWait();
            } else {
                Map<String, Object> record = selectedItems.get(0);
                user.getKoszyk_items().remove(record);
            }
        }
    }

    @FXML
    private void buyANDvalidate(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
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
                for (Map<String, Object> ticket : user.getKoszyk_items()) {
                    int count = 0;
                    for (int i = 0; i < Integer.parseInt((String) ticket.get("wybrane_ilosc")); i++) {
                        try (CallableStatement cstmt1 = conn.prepareCall("{? = call kupBilet(?,?,?)}");
                             CallableStatement cstmt2 = conn.prepareCall("{? = call skasujBilet(?,?)}");) {
                            cstmt1.registerOutParameter(1, Types.INTEGER);
                            cstmt1.setInt(2, user.getIdKonta());
                            cstmt1.setString(3, (String) ticket.get("wybrane_nazwa"));
                            cstmt1.setString(4, (String) ticket.get("wybrane_ulga"));
                            cstmt1.execute();
                            int success = cstmt1.getInt(1);
                            System.out.println("Zakupiono bilet o id=" + success);
                            cstmt2.registerOutParameter(1, Types.INTEGER);
                            cstmt2.setInt(2, user.getIdKonta());
                            cstmt2.setInt(3, success);
                            cstmt2.execute();
                            count++;
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
                            alert.setContentText("Podano nie numeryczne wartości!");
                            alert.showAndWait();
                        }
                    }
                    if (count == Integer.parseInt((String) ticket.get("wybrane_ilosc"))) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Insert Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Udane kupno biletów!");
                        alert.showAndWait();
                        close(event);
                    } else {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nieudane kupno biletów!");
                        alert.showAndWait();
                    }

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
    private void buy(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
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
                for(Map<String, Object> ticket : user.getKoszyk_items()){
                    int count = 0;
                    for(int i = 0; i< Integer.parseInt((String) ticket.get("wybrane_ilosc"));i++){
                        try (CallableStatement cstmt1 = conn.prepareCall("{? = call kupBilet(?,?,?)}");) {
                            cstmt1.registerOutParameter(1, Types.INTEGER);
                            cstmt1.setInt(2,user.getIdKonta());
                            cstmt1.setString(3,(String) ticket.get("wybrane_nazwa"));
                            cstmt1.setString(4,(String) ticket.get("wybrane_ulga"));
                            cstmt1.execute();
                            int success = cstmt1.getInt(1);
                            System.out.println("Zakupiono bilet o id="+success);
                            count++;
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
                    }
                    if (count == Integer.parseInt((String) ticket.get("wybrane_ilosc"))){
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Insert Information");
                        alert.setHeaderText(null);
                        alert.setContentText("Udane kupno biletów!");
                        alert.showAndWait();
                        close(event);
                    }else{
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Insert Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Nieudane kupno biletów!");
                        alert.showAndWait();
                    }
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
    private void swapWybierzUlgi(MouseEvent event) {
        if (event.getEventType() == MouseEvent.MOUSE_PRESSED) {
            if (event.getClickCount() == 2) {
                try {
                    ObservableList<Map<String, Object>> selectedItems = tv_TableBilety.getSelectionModel().getSelectedItems();
                    if (selectedItems.isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Delete Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Bilet nie został wybrany!");
                        alert.showAndWait();
                    } else {
                        Map<String,Object> record = selectedItems.get(0);
                        Swapper swapper = new Swapper(false, stage, user, null, null, "user/selectReliefCountFXML", null);
                        ((uSelectReliefCountFXController) swapper.getController()).myInitialize(record);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    @FXML
    private void swapUlgi(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/ulgiFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void swapLinie(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/linieFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void close(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                new Swapper(false, stage, user, null, null, "user/userHistoriaFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
