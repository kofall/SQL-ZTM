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
public class uTrasaGodzinyFXController implements Initializable {

    @FXML
    private Label lb_LineNumber;
    @FXML
    private Label lb_Przystanek;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private TableView<Map<String, Object>> tv_Table;
    @FXML
    private TableColumn<Map, String> tc_Godz;
    private Integer nr_linii;
    private String przystanek;
    private Integer id_szt;
    private Stage stage = null;
    private User user = null;
    private String backFXML = null;
    private Map<String, Object> record = null;
    private Map<String, Object> linia_record = null;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }
    private ObservableList<Map<String, Object>> table_items = FXCollections.<Map<String, Object>>observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tc_Godz.setCellValueFactory(new MapValueFactory<>("godz"));
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT godzina_odjazdu FROM godziny_odjazdow WHERE ID_PRZYSTANKU_W_TRASIE_SZT=?"); ){
                pstmt1.setInt(1,id_szt);
                ResultSet rs = pstmt1.executeQuery();
                while(rs.next()){
                    Map<String, Object> item = new HashMap<>();
                    Timestamp time =rs.getTimestamp(1);
                    item.put("godz",String.format("%02d",time.toLocalDateTime().getHour()) +":"+String.format("%02d",time.toLocalDateTime().getMinute() ));
                    item.put("time", time);
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

    public void myInitialize(Map<String, Object> record,Map<String, Object> linia_record, String backFXML, Integer nr) {
        this.backFXML = backFXML;
        this.record = record;
        this.linia_record = linia_record;
        przystanek = (String) record.get("nazwa");
        nr_linii = nr;
        id_szt = (Integer) record.get("id");

        lb_LineNumber.setText(nr_linii.toString());
        lb_Przystanek.setText(przystanek);
        initTables();
    }

    @FXML
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
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
                    try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT godzina_odjazdu FROM godziny_odjazdow WHERE ID_PRZYSTANKU_W_TRASIE_SZT=? AND TO_CHAR(godzina_odjazdu) LIKE '%'||?||'%'"); ){
                        pstmt1.setInt(1,id_szt);
                        pstmt1.setString(2,pattern);
                        ResultSet rs = pstmt1.executeQuery();
                        table_items.clear();
                        while(rs.next()){
                            Map<String, Object> item = new HashMap<>();
                            Timestamp time =rs.getTimestamp(1);
                            item.put("godz",String.format("%02d",time.toLocalDateTime().getHour()) +":"+String.format("%02d",time.toLocalDateTime().getMinute() ));

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
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(false, stage, user, null, null, "user/trasaFXML", null);
                ((uTrasaFXController) swapper.getController()).myInitialize(linia_record, backFXML);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
