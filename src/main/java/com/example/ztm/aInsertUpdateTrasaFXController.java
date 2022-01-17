/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.example.ztm;

import java.net.URL;
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
public class aInsertUpdateTrasaFXController implements Initializable {

    @FXML
    private TextField tf_NumberIn;
    @FXML
    private TextField tf_Name;
    @FXML
    private RadioButton rb_Yes;
    @FXML
    private RadioButton rb_No;

    private Stage stage = null;
    private User user = null;

    public void setStage(Stage stage) { this.stage = stage; }
    public void setUser(User user) { this.user = user; }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void myInitialize(Object record) {
        /*
        SET THE RECORD IF MODIFY
         */
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
        }
    }

    @FXML
    private void back(MouseEvent event) {
        if(event.getButton() == MouseButton.PRIMARY) {
            stage.close();
        }
    }
}
