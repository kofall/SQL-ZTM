/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
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
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aInsertUpdateTrasaFXController implements Initializable {

    @FXML
    private TextField tf_NumberIn;
    @FXML
    private ComboBox cb_Przystanek;
    @FXML
    private RadioButton rb_Yes;
    @FXML
    private RadioButton rb_No;

    private Stage stage = null;
    private User user = null;
    private Object prevController = null;
    private Integer id_szt;
    private Integer nr_lini;
    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }
    private ObservableList<String> przystanek_options =
            FXCollections.observableArrayList();
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
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
            try (PreparedStatement pstmt1 = conn.prepareStatement("SELECT nazwa FROM przystanek"); ){
                ResultSet rs = pstmt1.executeQuery();
                while(rs.next()){
                    przystanek_options.add(rs.getString(1));
                }
                rs.close();
                cb_Przystanek.setItems(przystanek_options);
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

    public void myInitialize(Object controller, Map<String, Object> record, Integer nr) {
        prevController = controller;
        if (record!=null){
            tf_NumberIn.setText((String) record.get("nr_w_trasie"));
            cb_Przystanek.setValue((String) record.get("nazwa"));
            String na_zadanie = (String) record.get("nazwa");
            if(na_zadanie.equals("Tak")){
                rb_Yes.setSelected(true);
                rb_No.setSelected(false);
            }else if (na_zadanie.equals("Nie")){
                rb_Yes.setSelected(false);
                rb_No.setSelected(true);
            }
            id_szt= (Integer) record.get("id");
        }

        nr_lini = nr;
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
    private void updateRBs(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(event.getSource() == rb_Yes) {
                rb_Yes.setSelected(true);
                rb_No.setSelected(false);
            }
            else {
                rb_Yes.setSelected(false);
                rb_No.setSelected(true);
            }
        }
    }

    @FXML
    private void addRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(tf_NumberIn.getText().equals("")|| cb_Przystanek.getSelectionModel().getSelectedItem().equals("")|| cb_Przystanek.getSelectionModel().getSelectedItem().equals("Przystanek")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Insert Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else{
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call dodajPrzystanekWTrasie(?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setInt(2,nr_lini);
                        cstmt1.setString(3,(String) cb_Przystanek.getSelectionModel().getSelectedItem());
                        cstmt1.setInt(4,Integer.parseInt(tf_NumberIn.getText()));
                        if(rb_Yes.isSelected()){
                            cstmt1.setString(5,"T");
                        }else if(rb_No.isSelected()){
                            cstmt1.setString(5,"N");
                        }
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Insert Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie przystanku do trasy!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Insert Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje już taki przystanek w trasie!");
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
            refreshTables();

        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            if(tf_NumberIn.getText().equals("")|| cb_Przystanek.getSelectionModel().getSelectedItem().equals("")|| cb_Przystanek.getSelectionModel().getSelectedItem().equals("Przystanek")){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Modify Error");
                alert.setHeaderText(null);
                alert.setContentText("Brakujące dane!");
                alert.showAndWait();
            }else{
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
                    try (CallableStatement cstmt1 = conn.prepareCall("{? = call zmodyfikujPrzystanekWTrasie(?,?,?,?,?)}");) {
                        cstmt1.registerOutParameter(1, Types.INTEGER);
                        cstmt1.setInt(2,id_szt);
                        cstmt1.setInt(3,nr_lini);
                        cstmt1.setString(4,(String) cb_Przystanek.getSelectionModel().getSelectedItem());
                        cstmt1.setInt(5,Integer.parseInt(tf_NumberIn.getText()));
                        if(rb_Yes.isSelected()){
                            cstmt1.setString(6,"T");
                        }else if(rb_No.isSelected()){
                            cstmt1.setString(6,"N");
                        }
                        cstmt1.execute();
                        int success = cstmt1.getInt(1);
                        if (success == 0) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Modify Information");
                            alert.setHeaderText(null);
                            alert.setContentText("Udane dodanie przystanku do trasy!");
                            alert.showAndWait();
                        } else if (success == 1) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Modify Error");
                            alert.setHeaderText(null);
                            alert.setContentText("Istnieje już taki przystanek w trasie!");
                            alert.showAndWait();

                        }
                    } catch (SQLException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
                        alert.setHeaderText(null);
                        alert.setContentText("Failed to execute query!");
                        alert.showAndWait();

                    }catch (NumberFormatException ex) {
                        System.out.println(ex.getMessage());
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Modify Error");
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
            refreshTables();

        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            refreshTables();
            stage.close();
        }
    }
}
