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
public class aTrasaGodzinyFXController implements Initializable {

    @FXML
    private TableView<?> tv_Table;
    @FXML
    private TextField tf_Pattern;
    @FXML
    private Label lb_LineNumber;
    @FXML
    private Label lb_Przystanek;

    private Integer nr_linii;
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

    }

    public void initTables() {

    }

    public void myInitialize(Map<String,Object> record) {

    }

    @FXML
    private void find(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {

        }
    }

    @FXML
    private void add(MouseEvent event) {
        if (event.getButton() == MouseButton.PRIMARY) {
            try {
                Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdateGodzinyOdjazdowFXML", "Odjazd");
                ((aInsertUpdateTrasaFXController) swapper.getController()).myInitialize(this, null,nr_linii);
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
                if(selectedItems.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Modify Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Przystanek nie został wybrany!");
                    alert.showAndWait();
                }else {
                    Map<String,Object> record = selectedItems.get(0);
                    Swapper swapper = new Swapper(true, null, user, null, null, "admin/insertUpdateGodzinyOdjazdowFXML", "Odjazd");
                    ((aInsertUpdateTrasaFXController) swapper.getController()).myInitialize(this, record,nr_linii);
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
                if(selectedItems.isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Modify Error");
                    alert.setHeaderText(null);
                    alert.setContentText("Przystanek nie został wybrany!");
                    alert.showAndWait();
                }else {
                    Map<String,Object> record = selectedItems.get(0);
                    Swapper swapper = new Swapper(true, null, user, null, null, "startup/sureFXML", null);
                    ((sSureFXController) swapper.getController()).myInitialize(this, record, "id", "id_przystanku_w_trasie_szt", "przystanek_w_trasie");
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
                new Swapper(false, stage, user, null, null, "admin/trasaFXML", null);
            } catch (IOException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
