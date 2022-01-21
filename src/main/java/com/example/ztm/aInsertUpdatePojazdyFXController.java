/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
public class aInsertUpdatePojazdyFXController implements Initializable {

    @FXML
    private TextField tf_Serial;
    @FXML
    private TextField tf_Registration;
    @FXML
    private TextField tf_StandingPlaces;
    @FXML
    private TextField tf_Seats;
    @FXML
    private TextField tf_Type;
    @FXML
    private RadioButton rb_Bus;
    @FXML
    private RadioButton rb_Tram;

    private Stage stage = null;
    private User user = null;
    private Object prevController = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object controller, Map<String, Object> record) {
        prevController = controller;
        if(record != null){
            tf_Serial.setText((String) record.get("nr_seryjny"));
            tf_Registration.setText((String) record.get("nr_rejestracyjny"));
            tf_Seats.setText((String) record.get("siedzace"));
            tf_StandingPlaces.setText((String) record.get("stojace"));
            String type = (String) record.get("typ");
            if (type.equals("Autobus")){
                rb_Bus.setSelected(true);
            }else if (type.equals("Tramwaj")){
                rb_Tram.setSelected(true);
            }
        }
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
            if(event.getSource() == rb_Bus) {
                rb_Bus.setSelected(true);
                rb_Tram.setSelected(false);
            }
            else {
                rb_Bus.setSelected(false);
                rb_Tram.setSelected(true);
            }
        }
    }

    @FXML
    private void addRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORD IF POSSIBLE
             */
            back(event);
        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            MODIFY THE RECORD IF EXISTS
             */
            back(event);
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
