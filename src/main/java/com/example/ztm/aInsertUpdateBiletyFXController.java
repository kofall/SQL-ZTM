/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.lang.reflect.InvocationTargetException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author kofal
 */
public class aInsertUpdateBiletyFXController implements Initializable {

    @FXML
    private TextField tf_name;
    @FXML
    private TextField tf_Time;
    @FXML
    private TextField tf_Area;
    @FXML
    private TextField tf_Price;

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

    public void myInitialize(Object controller, Object record) {
        prevController = controller;
        /*
        SET THE RECORD IF MODIFY
         */
    }

    private void refreshTables() {
        try {
            prevController.getClass().getMethod("initTables", Stage.class).invoke(prevController);
        } catch (IllegalAccessException e) {
            "".isEmpty();
        } catch (InvocationTargetException e) {
            "".isEmpty();
        } catch (NoSuchMethodException e) {
            "".isEmpty();
        }
    }

    @FXML
    private void addRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            ADD THE RECORD IF POSSIBLE
             */
            refreshTables();
            back(event);
        }
    }

    @FXML
    private void modifyRecord(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            /*
            MODIFY THE RECORD IF EXISTS
             */
            refreshTables();
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            stage.close();
        }
    }
}
