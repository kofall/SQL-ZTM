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
public class aPojazdyFXController implements Initializable {

    @FXML
    private TableView<Map<String,Object>> tv_Table;
    @FXML
    private TextField tf_Pattern;

    @FXML
    private TableColumn<Map, String> tc_Nr_seryjny;

    @FXML
    private TableColumn<Map, String> tc_Nr_rejestracyjny;

    @FXML
    private TableColumn<Map, String> tc_Typ;

    @FXML
    private TableColumn<Map, String> tc_Stojace;

    @FXML
    private TableColumn<Map, String> tc_Siedzace;

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
        tc_Nr_seryjny.setCellValueFactory(new MapValueFactory<>("nr_seryjny"));
        tc_Nr_rejestracyjny.setCellValueFactory(new MapValueFactory<>("nr_rejestracyjny"));
        tc_Typ.setCellValueFactory(new MapValueFactory<>("typ"));
        tc_Siedzace.setCellValueFactory(new MapValueFactory<>("siedzace"));
        tc_Stojace.setCellValueFactory(new MapValueFactory<>("stojace"));
    }

    public void initTables() {
        table_items.clear();
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM pojazd"); ){
                ResultSet rs = pstmt1.executeQuery();
                while(rs.next()){
                    Map<String, Object> item = new HashMap<>();
                    item.put("nr_seryjny",rs.getString(1));
                    item.put("stojace",rs.getInt(2));
                    item.put("siedzace",rs.getInt(3));
                    String type = rs.getString(4);
                    switch(type){
                        case "A":
                            item.put("typ","Autobus");
                            break;
                        case "T":
                            item.put("typ","Tramwaj");
                            break;
                        default:
                            item.put("typ", "Unknown");
                            break;
                    }
                    item.put("nr_rejestracyjny",rs.getString(5));
                    table_items.add(item);
                }
                rs.close();
                tv_Table.setItems(table_items);
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

    @FXML
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            String pattern = tf_Pattern.getText();
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
                try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM pojazd WHERE numer_seryjny LIKE '%'||?||'%'"); ){
                    pstmt1.setString(1,pattern);
                    ResultSet rs = pstmt1.executeQuery();
                    while(rs.next()){
                        Map<String, Object> item = new HashMap<>();
                        item.put("nr_seryjny",rs.getString(1));
                        item.put("stojace",rs.getInt(2));
                        item.put("siedzace",rs.getInt(3));
                        String type = rs.getString(4);
                        switch(type){
                            case "A":
                                item.put("typ","Autobus");
                                break;
                            case "T":
                                item.put("typ","Tramwaj");
                                break;
                            default:
                                item.put("typ", "Unknown");
                                break;
                        }
                        item.put("nr_rejestracyjny",rs.getString(5));
                        table_items.add(item);
                    }
                    rs.close();
                    tv_Table.setItems(table_items);
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
    }

    @FXML
    private void add(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdatePojazdyFXML", "Pojazd");
                ((aInsertUpdatePojazdyFXController) swapper.getController()).myInitialize(this, null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void modify(MouseEvent event) {
        if(event.getSource() == tv_Table) {
            if (!(event.getEventType() == MouseEvent.MOUSE_PRESSED &&
                    event.getClickCount() == 2)) {
                return;
            }
        }
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
                if(selectedItems.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Modify Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Pojazd nie został wybrany!");
                    alert.showAndWait();
                }else{
                    Map<String,Object> record = selectedItems.get(0);
                    Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdatePojazdyFXML", "Pojazd");
                    ((aInsertUpdatePojazdyFXController) swapper.getController()).myInitialize(this, record);
                }

            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }

    @FXML
    private void delete(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                ObservableList<Map<String, Object>> selectedItems = tv_Table.getSelectionModel().getSelectedItems();
                if(selectedItems.isEmpty()){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Pojazd nie został wybrany!");
                    alert.showAndWait();
                }else{
                    Map<String,Object> record = selectedItems.get(0);
                    Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                    ((sSureFXController) swapper.getController()).myInitialize(this, record,"nr_seryjny");
                }

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
                new Swapper(false, stage, user, null, null, "admin/adminFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
