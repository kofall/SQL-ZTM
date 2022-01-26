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
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class uUserHistoriaBiletInfoFXController implements Initializable {

    @FXML
    private Label lb_Name;
    @FXML
    private Label lb_Czas;
    @FXML
    private Label lb_Skasowany;
    @FXML
    private Label lb_PrzystanekStart;
    @FXML
    private Label lb_PrzystanekEnd;
    @FXML
    private Label lb_Strefa;
    @FXML
    private Label lb_Ulga;
    @FXML
    private Label lb_Procent;
    @FXML
    private Label lb_Cena;

    private Stage stage = null;
    private User user = null;
    private Integer idBiletu;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void initTables() {
        /*
        HERE INITIALIZE TABLE VALUES
         */
    }

    public void myInitialize(Map<String, Object> record) {
        idBiletu = (Integer) (record).get("id");
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT * FROM bilet INNER JOIN rodzaje_biletow ON bilet.rodzaje_biletow_nazwa = rodzaje_biletow.nazwa INNER JOIN ulgi ON bilet.ulgi_nazwa = ulgi.nazwa WHERE id_biletu=?"); ){
                pstmt1.setInt(1, idBiletu);
                ResultSet rs = pstmt1.executeQuery();
                while(rs.next()){
                    lb_Name.setText(rs.getString(8));
                    lb_Czas.setText(rs.getString(9));
                    String skasowany = rs.getString(2);
                    if(skasowany != null)
                        lb_Skasowany.setText("Tak");
                    else
                        lb_Skasowany.setText("Nie");
                    lb_PrzystanekStart.setText(rs.getString(7));
                    lb_PrzystanekEnd.setText(rs.getString(6));
                    lb_Strefa.setText(rs.getString(10));
                    lb_Ulga.setText(rs.getString(5));

                    lb_Cena.setText(String.valueOf(rs.getFloat(11)*rs.getFloat(13)/100));
                }
                rs.close();

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
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            stage.close();
        }
    }
}
